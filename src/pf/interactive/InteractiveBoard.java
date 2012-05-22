package pf.interactive;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import pf.analytics.Point;
import pf.animator.Animator;
import pf.board.Board;
import pf.board.BoardImpl;
import pf.board.GridType;
import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.PathImpl;
import pf.graph.Vertex;

/**
 * Extends {@link GameBoard}. Adds support for paths, touch mechanism, modes and
 * animators.
 * 
 * @author Adam Juraszek
 * 
 */
public class InteractiveBoard extends GameBoard {
	/**
	 * Default snapping policy which simply returns the nearest vertex if it is
	 * near enough
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class DefaultSnapPolicy implements SnapPolicy {

		private static final float dist = 0.1f;

		private final GridType gt;

		public DefaultSnapPolicy(GridType gt) {
			this.gt = gt;
		}

		@Override
		public Vertex request(GameBoard board, float x, float y, Point last) {
			Vertex v = board.getBoard().getNearest(x, y);
			if (v != null
					&& (v.getX() - x) * (v.getX() - x) + (v.getY() - y)
							* (v.getY() - y) < dist * gt.getUnitSize()
							* gt.getUnitSize()) {
				return v;
			}
			return null;
		}

	}

	/**
	 * Provides control over touch mechanism
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected class TouchSupport implements MouseListener, MouseMotionListener {

		protected Path path = null;

		protected Vertex nearest = null;

		protected Vertex last;

		protected int cursor = -2;

		public TouchSupport() {
		}

		/**
		 * Forces to cancel touch.
		 */
		public void cancelTouch() {
			if (isTouchInProgress()) {
				fireTouchCancelled(new TouchEvent(InteractiveBoard.this, path,
						null, null));
				path = null;
			}
		}

		/**
		 * @return true if touch is in progress, false otherwise
		 */
		public boolean isTouchInProgress() {
			return path != null;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			nearest = getNearest(e.getPoint().getX(), e.getPoint().getY());
			if (isTouchInProgress() && nearest != null) {
				Edge edge = last.edgeToVertex(nearest);
				if (edge != null) {
					if (path.length() > 0 && path.getLast().equals(edge)) {
						if (isTouchReturnAllowed()) {
							path.shorten();
							last = nearest;
							fireTouchShorter(new TouchEvent(
									InteractiveBoard.this, path, nearest, edge));
						}
					} else {
						path.extend(edge);
						last = nearest;
						fireTouchLonger(new TouchEvent(InteractiveBoard.this,
								path, nearest, edge));
					}
				}
			}
			setCursor();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			nearest = getNearest(e.getPoint().getX(), e.getPoint().getY());
			setCursor();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (nearest != null && isTouchActive()) {
				path = new PathImpl(nearest);
				last = nearest;
				fireTouchStarted(new TouchEvent(InteractiveBoard.this, path,
						last, null));
			}
			setCursor();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isTouchInProgress()) {
				fireTouchEnded(new TouchEvent(InteractiveBoard.this, path,
						null, null));
				path = null;
			}
			nearest = getNearest(e.getPoint().getX(), e.getPoint().getY());
			setCursor();
		}

		/**
		 * @param x
		 * @param y
		 * @return nearest vertex to specified coordinates or null
		 */
		private Vertex getNearest(double x, double y) {
			float xx = translateXFromScreen((int) x);
			float yy = translateYFromScreen((int) y);
			return getSnapPolicy().request(InteractiveBoard.this, xx, yy, null);
		}

		/**
		 * Sets cursor for current state of touch.
		 */
		private void setCursor() {
			int setCursor = -1;
			if (isTouchInProgress()) {
				if (cursor != Cursor.MOVE_CURSOR) {
					setCursor = Cursor.MOVE_CURSOR;
				}
			} else if (nearest != null) {
				if (cursor != Cursor.HAND_CURSOR) {
					setCursor = Cursor.HAND_CURSOR;
				}
			} else {
				if (cursor != Cursor.DEFAULT_CURSOR) {
					setCursor = Cursor.DEFAULT_CURSOR;
				}
			}

			if (setCursor != -1) {
				InteractiveBoard.this.setCursor(Cursor
						.getPredefinedCursor(setCursor));
			}
		}

	}

	private static final long serialVersionUID = 1L;
	private GameMode mode = GameMode.SHOW;

	protected EventListenerList ell;

	boolean touchActive = false;
	boolean touchReturnAllowed = false;

	protected Animator animator = null;

	private boolean paintPaths = false;

	private TouchSupport ts;

	protected Map<Path, PathPainter> paths;

	private SnapPolicy snapping;
	private boolean editable = false;

	public InteractiveBoard() {
		super();
		ell = new EventListenerList();
		paths = new HashMap<Path, PathPainter>();
		ts = new TouchSupport();
		setBoard(null);
	}

	/**
	 * Adds a new game mode listener
	 * 
	 * @param l
	 */
	public synchronized void addGameModeListener(GameModeListener l) {
		ell.add(GameModeListener.class, l);
	}

	/**
	 * Adds a new path. This path will not be painted because it has no assigned
	 * painter.
	 * 
	 * @param path
	 */
	public void addPath(Path path) {
		if (board == null) {
			throw new IllegalStateException();
		}
		synchronized (paths) {
			paths.put(path, null);
		}
	}

	/**
	 * Adds a new touch listener.
	 * 
	 * @param l
	 */
	public synchronized void addTouchListener(TouchListener l) {
		ell.add(TouchListener.class, l);
	}

	/**
	 * Asks whether can switch mode from current to m
	 * 
	 * @param m
	 *            mode to switch to
	 * @return true if possible to switch, false otherwise
	 */
	public boolean can(GameMode m) {
		switch (m) {
		case EDIT:
			return canEdit();
		case RUN:
			return canRun();
		case SHOW:
			return canShow();
		}
		return false;
	}

	/**
	 * Forces to cancel touch.
	 */
	public void cancelTouch() {
		ts.cancelTouch();
	}

	/**
	 * @return true if it is possible to switch mode to edit, false otherwise
	 */
	public boolean canEdit() {
		if (!isEditable()) {
			return false;
		}
		switch (mode) {
		case EDIT:
			return false;
		case RUN:
			return false;
		case SHOW:
			return true;
		}
		return false;
	}

	/**
	 * @return true if it is possible to switch mode to run, false otherwise
	 */
	public boolean canRun() {
		if (animator == null) {
			return false;
		}
		switch (mode) {
		case EDIT:
			return false;
		case RUN:
			return false;
		case SHOW:
			return true;
		}
		return false;
	}

	/**
	 * @return true if it is possible to switch mode to show, false otherwise
	 */
	public boolean canShow() {
		switch (getMode()) {
		case EDIT:
			return true;
		case RUN:
			return true;
		case SHOW:
			return false;
		}
		return false;
	}

	/**
	 * changes mode to edit, propagates event.
	 */
	public void edit() {
		if (isEdit()) {
			return;
		}
		if (!canEdit()) {
			throw new IllegalStateException();
		}
		GameMode m = mode;
		mode = GameMode.EDIT;
		if (m.equals(GameMode.SHOW)) {
			fromShowToEdit();
		}
		repaint();
		fireModeEdit(new GameModeEvent(this, m, mode));
	}

	/**
	 * @return current animator
	 */
	public Animator getAnimator() {
		return animator;
	}

	/**
	 * @return current mode
	 */
	public GameMode getMode() {
		return mode;
	}

	/**
	 * @param path
	 * @return path painter assigned to paint path
	 */
	public PathPainter getPathPainter(Path path) {
		return paths.get(path);
	}

	/**
	 * @return current snap policy for snapping to vertices
	 */
	public SnapPolicy getSnapPolicy() {
		return snapping;
	}

	/**
	 * @return whether current mode is edit
	 */
	public boolean isEdit() {
		return getMode() == GameMode.EDIT;
	}

	/**
	 * @return whether is possible to edit this board
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @return whether paths should be painted
	 */
	public boolean isPaintPaths() {
		return paintPaths;
	}

	/**
	 * @return whether current mode is run
	 */
	public boolean isRun() {
		return getMode() == GameMode.RUN;
	}

	/**
	 * @return whether current mode is show
	 */
	public boolean isShow() {
		return getMode() == GameMode.SHOW;
	}

	/**
	 * @return whether touch mechanism is active and touch event should be
	 *         propagated
	 */
	public boolean isTouchActive() {
		return touchActive;
	}

	/**
	 * @return whether touch is in progress
	 */
	public boolean isTouchInProgress() {
		return ts != null ? ts.isTouchInProgress() : false;
	}

	/**
	 * @return whether is possible to make path shorter
	 */
	public boolean isTouchReturnAllowed() {
		return touchReturnAllowed;
	}

	/**
	 * Removes specified game mode listener.
	 * 
	 * @param l
	 */
	public synchronized void removeGameModeListener(GameModeListener l) {
		ell.remove(GameModeListener.class, l);
	}

	/**
	 * Removes specified path and its painter.
	 * 
	 * @param path
	 */
	public void removePath(Path path) {
		synchronized (paths) {
			paths.remove(path);
		}
	}

	/**
	 * Removes all paths and their painters.
	 */
	public void removePaths() {
		synchronized (paths) {
			paths.clear();
		}
	}

	/**
	 * Removes specified touch listener.
	 * 
	 * @param l
	 */
	public synchronized void removeTouchListener(TouchListener l) {
		ell.remove(TouchListener.class, l);
	}

	/**
	 * Repaints specified edge, repaint area is smallest possible.
	 * 
	 * @param e
	 */
	public void repaintEdge(Edge e) {
		if (getEdgesPainter() == null) {
			return;
		}
		Rectangle r = getEdgesPainter().getBounds(this, e);
		if (getVerticesPainter() != null) {
			Rectangle r1 = getVerticesPainter().getBounds(this, e.getV1());
			Rectangle r2 = getVerticesPainter().getBounds(this, e.getV2());
			r = r.union(r1).union(r2);
		}
		repaint(r);
	}

	/**
	 * Repaints specified vertex, repaint area is smallest possible.
	 * 
	 * @param v
	 */
	public void repaintVertex(Vertex v) {
		if (getVerticesPainter() == null) {
			return;
		}
		Rectangle r = getVerticesPainter().getBounds(this, v);
		repaint(r);
	}

	/**
	 * Switches mode to run and propagates game mode event.
	 */
	public void run() {
		if (isRun()) {
			return;
		}
		if (!canRun()) {
			throw new IllegalStateException();
		}
		GameMode m = mode;
		mode = GameMode.RUN;
		if (m.equals(GameMode.SHOW)) {
			fromShowToRun();
		}
		fireModeRun(new GameModeEvent(this, m, mode));
	}

	/**
	 * Sets a new animator.
	 * 
	 * @param animator
	 */
	public void setAnimator(Animator animator) {
		if (this.animator != null) {
			this.animator.stop();
		}
		this.animator = animator;
	}

	/**
	 * Sets a new board. Most properties are reseted.
	 */
	@Override
	public void setBoard(Board board) {
		Board old = this.board;
		super.setBoard(board);
		setTouchActive(false);
		setTouchReturnAllowed(false);

		setAnimator(null);
		setMode(GameMode.SHOW);
		setEditable(false);

		removePaths();
		if (board != null) {
			setSnapPolicy(new DefaultSnapPolicy(board.getGrid().getGridType()));
			if (old == null) {
				addMouseListener(ts);
				addMouseMotionListener(ts);
			}
		} else {
			removeMouseListener(ts);
			removeMouseMotionListener(ts);
		}
		repaint();
	}

	/**
	 * Sets whether this board can be edited.
	 * 
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		if (getMode().equals(GameMode.EDIT)) {
			throw new IllegalStateException();
		}
		this.editable = editable;
	}

	/**
	 * Sets desired mode m. Calls appropriate method.
	 * 
	 * @param m
	 *            mode to switch to
	 */
	public void setMode(GameMode m) {
		switch (m) {
		case EDIT:
			edit();
			break;
		case RUN:
			run();
			break;
		case SHOW:
			show();
			break;
		}
	}

	/**
	 * Sets whether paths should be painted.
	 * 
	 * @param paintPaths
	 */
	public void setPaintPaths(boolean paintPaths) {
		this.paintPaths = paintPaths;
	}

	/**
	 * Sets path painter for specified path.
	 * 
	 * @param path
	 * @param pathPainter
	 */
	public void setPathPainter(Path path, PathPainter pathPainter) {
		synchronized (paths) {
			if (!paths.containsKey(path)) {
				throw new IllegalArgumentException();
			}
			paths.put(path, pathPainter);
		}
	}

	/**
	 * Sets snapping policy.
	 * 
	 * @param snapping
	 */
	public void setSnapPolicy(SnapPolicy snapping) {
		if (snapping == null) {
			throw new IllegalArgumentException();
		}
		this.snapping = snapping;
	}

	/**
	 * Sets whether touch mechanism should be active and propagate events.
	 * 
	 * @param active
	 */
	public void setTouchActive(boolean active) {
		if (isTouchInProgress()) {
			cancelTouch();
		}
		touchActive = active;
	}

	/**
	 * Sets whether making touch path shorter is allowed.
	 * 
	 * @param allowed
	 */
	public void setTouchReturnAllowed(boolean allowed) {
		touchReturnAllowed = allowed;
	}

	/**
	 * Switches to show mode and propagates game mode event.
	 * <p>
	 * This is a collision of names with {@link Component#show()}
	 * 
	 * This method has nothing to do with {@link Component#show()}, name collision occurred after refactor.
	 */
	@SuppressWarnings("deprecation")
	public void show() {
		if (isShow()) {
			return;
		}
		if (!canShow()) {
			throw new IllegalStateException();
		}
		GameMode m = mode;
		mode = GameMode.SHOW;
		if (m.equals(GameMode.EDIT)) {
			fromEditToShow();
		} else if (m.equals(GameMode.RUN)) {
			fromRunToShow();
		}
		repaint();
		fireModeShow(new GameModeEvent(this, m, mode));
	}

	/**
	 * Method called whenever mode is switched from edit to show
	 */
	protected void fromEditToShow() {
		board = BoardImpl.createShowBoard(getBoard());
	}

	/**
	 * Method called whenever mode is switched from run to show
	 */
	protected void fromRunToShow() {
		if (animator != null) {
			animator.stop();
		}
	}

	/**
	 * Method called whenever mode is switched from show to edit
	 */
	protected void fromShowToEdit() {
		board = BoardImpl.createEditBoard(getBoard());
	}

	/**
	 * Method called whenever mode is switched from show to run
	 */
	protected void fromShowToRun() {
	}

	/**
	 * Adds {@link #paintPaths(Graphics2D)} to be called whenever repaint
	 * occurs.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		paintPaths(g2d);
	}

	/**
	 * Paints all paths with assigned painters.
	 * 
	 * @param g2d
	 */
	protected void paintPaths(Graphics2D g2d) {
		synchronized (paths) {
			for (Path p : paths.keySet()) {
				PathPainter pp = paths.get(p);
				if (pp != null && isPaintPaths()) {
					Path2D<?> p2d = pp.createPath2D();
					pp.paintPath(g2d, p2d);
				}
			}
		}
	}

	/**
	 * Informs {@link GameModeListener}s about change of mode to edit.
	 * 
	 * @param e
	 */
	void fireModeEdit(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modeEdit(e);
		}
	}

	/**
	 * Informs {@link GameModeListener}s about change of mode to run.
	 * 
	 * @param e
	 */
	void fireModeRun(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modeRun(e);
		}
	}

	/**
	 * Informs {@link GameModeListener}s about change of mode to show.
	 * 
	 * @param e
	 */
	void fireModeShow(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modeShow(e);
		}
	}

	/**
	 * Informs {@link TouchListener}s about canceled touch.
	 * 
	 * @param e
	 */
	void fireTouchCancelled(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchCancelled(e);
		}
	}

	/**
	 * Informs {@link TouchListener}s about ended touch.
	 * 
	 * @param e
	 */
	void fireTouchEnded(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchEnded(e);
		}
	}

	/**
	 * Informs {@link TouchListener}s about touch made longer.
	 * 
	 * @param e
	 */
	void fireTouchLonger(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchLonger(e);
		}
	}

	/**
	 * Informs {@link TouchListener}s about touch made shorter.
	 * 
	 * @param e
	 */
	void fireTouchShorter(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchShorter(e);
		}
	}

	/**
	 * Informs {@link TouchListener}s about touch started.
	 * 
	 * @param e
	 */
	void fireTouchStarted(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchStarted(e);
		}
	}

}
