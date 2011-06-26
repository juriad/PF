package pf.gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.event.EventListenerList;

import pf.board.Board;
import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.PathImpl;
import pf.graph.Vertex;

public class InteractiveBoard extends GameBoard implements Iterable<Path> {
	public static class DefaultSnapPolicy implements SnapPolicy {

		private static final float dist = 0.1f;

		public DefaultSnapPolicy() {
		}

		@Override
		public Vertex request(GameBoard board, float x, float y) {
			Vertex v = board.getBoard().getNearest(x, y);
			if ((v.getX() - x) * (v.getX() - x) + (v.getY() - y)
					* (v.getY() - y) < dist)
				return v;
			return null;
		}

	}

	protected class TouchSupport implements MouseListener, MouseMotionListener {

		protected Path path = null;

		protected Vertex nearest;

		protected Vertex last;

		public TouchSupport() {

		}

		public void cancelTouch() {
			fireTouchCancelled(new TouchEvent(InteractiveBoard.this, path,
					null, null));
			path = null;
		}

		private Vertex getNearest(Point p) {
			float x = translateXFromScreen((int) p.getX());
			float y = translateYFromScreen((int) p.getY());
			return getSnapPolicy().request(InteractiveBoard.this, x, y);
		}

		public boolean isTouchInProgress() {
			return path != null;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			nearest = getNearest(e.getPoint());
			if (isTouchInProgress() && nearest != null) {
				Edge edge = last.edgeToVertex(nearest);
				if (edge != null)
					if (path.getLast().equals(edge)) {
						if (isTouchReturnAllowed()) {
							path.shorten(edge);
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

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			nearest = getNearest(e.getPoint());
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
			nearest = getNearest(e.getPoint());
			setCursor();
		}

		private void setCursor() {
			if (isTouchInProgress())
				InteractiveBoard.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.MOVE_CURSOR));
			else if (nearest != null)
				InteractiveBoard.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
			else
				InteractiveBoard.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

	}

	private static final long serialVersionUID = 1L;
	private GameMode mode = GameMode.SHOW;

	protected final EventListenerList ell;

	boolean touchActive = false;
	boolean touchReturnAllowed = false;

	protected Animator animator = null;

	private PathPainter defaultPathPainter = null;

	private boolean paintPaths = false;

	private final TouchSupport ts;

	protected Map<Path, PathPainter> paths;

	private SnapPolicy snapping;

	public InteractiveBoard(Board board) {
		super(board);
		ell = new EventListenerList();
		paths = new HashMap<Path, PathPainter>();
		ts = new TouchSupport();
		this.addMouseListener(ts);
		this.addMouseMotionListener(ts);
		snapping = new DefaultSnapPolicy();
	}

	public synchronized void addGameModeListener(GameModeListener l) {
		ell.add(GameModeListener.class, l);
	}

	public void addPath(Path path) {
		paths.put(path, getDefaultPathPainter());
	}

	public synchronized void addTouchListener(TouchListener l) {
		ell.add(TouchListener.class, l);
	}

	public void cancelTouch() {
		ts.cancelTouch();
	}

	public void edit() {
		switch (mode) {
		case EDIT:
			break;
		case PAUSE:
			throw new IllegalStateException();
		case RUN:
			throw new IllegalStateException();
		case SHOW:
			fromShowToEdit();
			break;
		}
	}

	void fireModeEdit(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class))
			l.modeEdit(e);
	}

	void fireModePause(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class))
			l.modePause(e);
	}

	void fireModeRun(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class))
			l.modeRun(e);
	}

	void fireModeShow(GameModeEvent e) {
		for (GameModeListener l : ell.getListeners(GameModeListener.class))
			l.modeShow(e);
	}

	void fireTouchCancelled(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class))
			l.touchCancelled(e);
	}

	void fireTouchEnded(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class))
			l.touchEnded(e);
	}

	void fireTouchLonger(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class))
			l.touchLonger(e);
	}

	void fireTouchShorter(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class))
			l.touchShorter(e);
	}

	void fireTouchStarted(TouchEvent e) {
		for (TouchListener l : ell.getListeners(TouchListener.class))
			l.touchStarted(e);
	}

	private void fromEditToShow() {
		// TODO autogen method: fromEditToShow

	}

	private void fromPauseToRun() {
		mode = GameMode.RUN;
	}

	private void fromPauseToShow() {
		animator.stop();
		mode = GameMode.SHOW;
	}

	private void fromRunToPause() {
		animator.pause();
		mode = GameMode.PAUSE;
	}

	private void fromRunToShow() {
		animator.stop();
		mode = GameMode.SHOW;
	}

	private void fromShowToEdit() {
		// TODO

	}

	private void fromShowToRun() {
		animator.run();
		mode = GameMode.RUN;
	}

	public Animator getAnimator() {
		return animator;
	}

	public PathPainter getDefaultPathPainter() {
		return defaultPathPainter;
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

	public boolean isPaintPaths() {
		return paintPaths;
	}

	public boolean isPause() {
		return getMode() == GameMode.PAUSE;
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
		return ts.isTouchInProgress();
	}

	public boolean isTouchReturnAllowed() {
		return touchReturnAllowed;
	}

	@Override
	public Iterator<Path> iterator() {
		return paths.keySet().iterator();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		paintPaths(g2d);
	}

	protected void paintPaths(Graphics2D g2d) {
		for (Path p : this) {
			PathPainter pp = paths.get(p);
			if (pp != null && isPaintPaths())
				pp.paintPath(g2d, this, p);
		}
	}

	public void pause() {
		switch (mode) {
		case EDIT:
			throw new IllegalStateException();
		case PAUSE:
			break;
		case RUN:
			fromRunToPause();
			break;
		case SHOW:
			throw new IllegalStateException();
		}
	}

	public synchronized void removeGameModeListener(GameModeListener l) {
		ell.remove(GameModeListener.class, l);
	}

	public void removePath(Path path) {
		paths.remove(path);
	}

	public synchronized void removeTouchListener(TouchListener l) {
		ell.remove(TouchListener.class, l);
	}

	public void run() {
		switch (mode) {
		case EDIT:
			throw new IllegalStateException();
		case PAUSE:
			fromPauseToRun();
			break;
		case RUN:
			break;
		case SHOW:
			fromShowToRun();
			break;
		}
	}

	public void setAnimator(Animator animator) {
		if (isRun() || isPause())
			throw new IllegalStateException();
		this.animator = animator;
	}

	public void setDefaultPathPainter(PathPainter pathPainter) {
		this.defaultPathPainter = pathPainter;
	}

	public void setMode(GameMode m) {
		switch (m) {
		case EDIT:
			edit();
			break;
		case PAUSE:
			pause();
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
		paths.put(path, pathPainter);
	}

	public void setSnapPolicy(SnapPolicy snapping) {
		this.snapping = snapping;
	}

	public void setTouchActive(boolean active) {
		cancelTouch();
		this.setTouchActive(active);
	}

	public void setTouchReturnAllowed(boolean allowed) {
		this.touchReturnAllowed = allowed;
	}

	@Override
	public void show() {
		switch (mode) {
		case EDIT:
			fromEditToShow();
			break;
		case PAUSE:
			fromPauseToShow();
			break;
		case RUN:
			fromRunToShow();
		case SHOW:
			break;
		}
	}

}
