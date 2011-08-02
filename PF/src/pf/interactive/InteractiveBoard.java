package pf.interactive;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.event.EventListenerList;

import pf.analytics.Point;
import pf.board.Board;
import pf.board.BoardImpl;
import pf.board.GridType;
import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.PathImpl;
import pf.graph.Vertex;
import pf.reimpl.Path2D;
import animator.Animator;

public class InteractiveBoard extends GameBoard implements Iterable<Path> {
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

	protected class TouchSupport implements MouseListener, MouseMotionListener {

		protected Path path = null;

		protected Vertex nearest = null;

		protected Vertex last;

		protected int cursor = -2;

		public TouchSupport() {
		}

		public void cancelTouch() {
			if (isTouchInProgress()) {
				fireTouchCancelled(new TouchEvent(InteractiveBoard.this, path,
						null, null));
				path = null;
			}
		}

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
				path = new PathImpl();
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

		private Vertex getNearest(double x, double y) {
			float xx = translateXFromScreen((int) x);
			float yy = translateYFromScreen((int) y);
			return getSnapPolicy().request(InteractiveBoard.this, xx, yy, null);
		}

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

	public synchronized void addGameModeListener(GameModeListener l) {
		ell.add(GameModeListener.class, l);
	}

	public void addPath(Path path) {
		if (board == null) {
			throw new IllegalStateException();
		}
		paths.put(path, null);
	}

	public synchronized void addTouchListener(TouchListener l) {
		ell.add(TouchListener.class, l);
	}

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

	public void cancelTouch() {
		ts.cancelTouch();
	}

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

	public boolean canPause() {
		if (animator == null) {
			return false;
		}
		switch (mode) {
		case EDIT:
			return false;
		case RUN:
			return true;
		case SHOW:
			return false;
		}
		return false;
	}

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

	public Animator getAnimator() {
		return animator;
	}

	public GameMode getMode() {
		return mode;
	}

	public PathPainter getPathPainter(Path path) {
		return paths.get(path);
	}

	public SnapPolicy getSnapPolicy() {
		return snapping;
	}

	public boolean isEdit() {
		return getMode() == GameMode.EDIT;
	}

	public boolean isEditable() {
		return editable;
	}

	public boolean isPaintPaths() {
		return paintPaths;
	}

	public boolean isRun() {
		return getMode() == GameMode.RUN;
	}

	public boolean isShow() {
		return getMode() == GameMode.SHOW;
	}

	public boolean isTouchActive() {
		return touchActive;
	}

	public boolean isTouchInProgress() {
		return ts != null ? ts.isTouchInProgress() : false;
	}

	public boolean isTouchReturnAllowed() {
		return touchReturnAllowed;
	}

	@Override
	public Iterator<Path> iterator() {
		return paths.keySet().iterator();
	}

	public synchronized void removeGameModeListener(GameModeListener l) {
		ell.remove(GameModeListener.class, l);
	}

	public void removePath(Path path) {
		paths.remove(path);
	}

	public void removePaths() {
		paths.clear();
	}

	public synchronized void removeTouchListener(TouchListener l) {
		ell.remove(TouchListener.class, l);
	}

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

	public void repaintVertex(Vertex v) {
		if (getVerticesPainter() == null) {
			return;
		}
		Rectangle r = getVerticesPainter().getBounds(this, v);
		repaint(r);
	}

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

	public void setAnimator(Animator animator) {
		if (isRun()) {
			throw new IllegalStateException();
		}
		this.animator = animator;
	}

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

	public void setEditable(boolean editable) {
		if (getMode().equals(GameMode.EDIT)) {
			throw new IllegalStateException();
		}
		this.editable = editable;
	}

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

	public void setPaintPaths(boolean paintPaths) {
		this.paintPaths = paintPaths;
	}

	public void setPathPainter(Path path, PathPainter pathPainter) {
		if (!paths.containsKey(path)) {
			throw new IllegalArgumentException();
		}
		paths.put(path, pathPainter);
	}

	public void setSnapPolicy(SnapPolicy snapping) {
		if (snapping == null) {
			throw new IllegalArgumentException();
		}
		this.snapping = snapping;
	}

	public void setTouchActive(boolean active) {
		if (isTouchInProgress()) {
			cancelTouch();
		}
		touchActive = active;
	}

	public void setTouchReturnAllowed(boolean allowed) {
		touchReturnAllowed = allowed;
	}

	@Override
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

	private void fromEditToShow() {
		board = BoardImpl.createShowBoard(getBoard());
	}

	private void fromRunToShow() {
		animator.stop();
	}

	private void fromShowToEdit() {
		board = BoardImpl.createEditBoard(getBoard());
	}

	private void fromShowToRun() {
		animator.run();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		paintPaths(g2d);
	}

	protected void paintPaths(Graphics2D g2d) {
		for (Path p : this) {
			PathPainter pp = paths.get(p);
			if (pp != null && isPaintPaths()) {
				Path2D<?> p2d = pp.createPath2D();
				pp.paintPath(g2d, p2d);
			}
		}
	}

	void fireModeEdit(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modeEdit(e);
		}
	}

	void fireModePause(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modePause(e);
		}
	}

	void fireModeRun(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modeRun(e);
		}
	}

	void fireModeShow(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class)) {
			l.modeShow(e);
		}
	}

	void fireTouchCancelled(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchCancelled(e);
		}
	}

	void fireTouchEnded(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchEnded(e);
		}
	}

	void fireTouchLonger(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchLonger(e);
		}
	}

	void fireTouchShorter(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchShorter(e);
		}
	}

	void fireTouchStarted(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class)) {
			l.touchStarted(e);
		}
	}

}
