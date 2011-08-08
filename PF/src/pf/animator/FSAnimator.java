package pf.animator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import pf.graph.Direction;
import pf.graph.Edge;
import pf.graph.Vertex;
import pf.interactive.GameMode;
import pf.interactive.GameModeEvent;
import pf.interactive.GameModeListener;
import pf.interactive.InteractiveBoard;
import pf.interactive.TouchAdapter;
import pf.interactive.TouchEvent;

/**
 * One extra animator to show possibilities of this application.
 * <p>
 * Animates dfs and bfs from whatever vertex user chooses as a start.
 * 
 * @author Adam Juraszek
 * 
 */
public class FSAnimator extends StepAnimator {

	/**
	 * 
	 * Factory for {@link FSAnimator}
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class FSAnimatorFactory implements AnimatorFactory {

		private static volatile FSAnimatorFactory factory = null;

		public static FSAnimatorFactory getFactory() {
			if (factory == null) {
				synchronized (FSAnimatorFactory.class) {
					if (factory == null) {
						factory = new FSAnimatorFactory();
					}
				}
			}
			return factory;
		}

		private FSAnimatorFactory() {
		}

		@Override
		public Animator newInstance(InteractiveBoard board) {
			return new FSAnimator(board);
		}

		@Override
		public String toString() {
			return "Animator ilustrates progress of dfs and bfs, select start vertex";
		}
	}

	/**
	 * Listens to mode changes in {@link InteractiveBoard} to set enabled menu
	 * items.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public class ModeListener implements GameModeListener {

		@Override
		public void modeEdit(GameModeEvent e) {
			updateControl();
		}

		@Override
		public void modeRun(GameModeEvent e) {
			updateControl();
		}

		@Override
		public void modeShow(GameModeEvent e) {
			updateControl();
		}
	}

	/**
	 * Represents edge with specified vertex from which it starts.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	private static class OrientedEdge {
		private final Vertex from;
		private final Edge e;

		public OrientedEdge(Edge e, Vertex from) {
			this.e = e;
			this.from = from;
		}

		public Edge getEdge() {
			return e;
		}

		public Vertex getFrom() {
			return from;
		}

		public Vertex getTo() {
			return e.getOther(from);
		}
	}

	/**
	 * Switches to bfs mode.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected class BFSAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public BFSAction() {
			super("BFS");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_B);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fs = FS.BFS;
		}
	}

	/**
	 * Switches to dfs mode.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected class DFSAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public DFSAction() {
			super("DFS");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fs = FS.DFS;
		}
	}

	/**
	 * Distinguish dfs and bfs modes.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected static enum FS {
		DFS {
			@Override
			public void addEdge(Deque<OrientedEdge> d, OrientedEdge e) {
				d.addLast(e);
			}

			@Override
			public OrientedEdge removeEdge(Deque<OrientedEdge> d) {
				return d.removeLast();
			}
		},
		BFS {
			@Override
			public void addEdge(Deque<OrientedEdge> d, OrientedEdge e) {
				d.addLast(e);

			}

			@Override
			public OrientedEdge removeEdge(Deque<OrientedEdge> d) {
				return d.removeFirst();
			}
		};

		/**
		 * Adds edge to data structure.
		 * 
		 * @param d
		 * @param e
		 */
		public abstract void addEdge(Deque<OrientedEdge> d, OrientedEdge e);

		/**
		 * Removes edge from data structure.
		 * 
		 * @param d
		 * @return
		 */
		public abstract OrientedEdge removeEdge(Deque<OrientedEdge> d);
	}

	/**
	 * Orders or randomizes unused edges from a vertex. Edges are stored in
	 * hashmap (hashset), so their order is semi-randomized.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected enum Order {
		RANDOMIZED {
			@Override
			public List<OrientedEdge> convertIterator(Vertex from,
					Iterator<Edge> i) {
				return new RandomizedIterator<OrientedEdge>(convert(from, i))
						.getList();
			}
		},
		ORDERED {
			@Override
			public List<OrientedEdge> convertIterator(Vertex from,
					Iterator<Edge> i) {
				Comparator<OrientedEdge> comp = new Comparator<OrientedEdge>() {
					@Override
					public int compare(OrientedEdge o1, OrientedEdge o2) {
						Direction d1 = o1.getEdge().getDirection(o1.getFrom());
						double a1 = getAngle(d1.getDx(), d1.getDy());
						Direction d2 = o2.getEdge().getDirection(o2.getFrom());
						double a2 = getAngle(d2.getDx(), d2.getDy());
						return Double.compare(a1, a2);
					}
				};

				return new OrderedIterator<FSAnimator.OrientedEdge>(convert(
						from, i), comp).getList();
			}

			private double getAngle(int x, int y) {
				if (x > 0) {
					if (y >= 0) {
						return Math.atan(y / x);
					}
					return Math.atan(y / x) + 2 * Math.PI;
				} else if (x == 0) {
					if (y >= 0) {
						return Math.PI / 2;
					}
					return 3 * Math.PI / 2;
				} else {
					double angle = getAngle(-x, -y) + Math.PI;
					return angle >= 2 * Math.PI ? angle - 2 * Math.PI : angle;
				}
			}
		};

		public static Order getOrder(boolean randomized) {
			return randomized ? RANDOMIZED : ORDERED;
		}

		private static Iterator<OrientedEdge> convert(Vertex from,
				Iterator<Edge> i) {
			ArrayList<OrientedEdge> items = new ArrayList<OrientedEdge>();
			while (i.hasNext()) {
				items.add(new OrientedEdge(i.next(), from));
			}
			return items.iterator();
		}

		/**
		 * Converts itarator to sorted/randomized list.
		 * 
		 * @param from
		 * @param i
		 * @return
		 */
		public abstract List<OrientedEdge> convertIterator(Vertex from,
				Iterator<Edge> i);
	}

	/**
	 * Allows user to choose whether edges are queued randomly or in a specific
	 * order.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected class RandomizedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public RandomizedAction() {
			super("Randomized");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setRandomized(randomizedMenuItem.isSelected());
		}
	}

	static {
		Animators.getInstance().addAnimator(FSAnimatorFactory.getFactory());
	}

	protected FS fs;
	protected final InteractiveBoard board;
	private JCheckBoxMenuItem randomizedMenuItem;
	private JRadioButtonMenuItem dfsMenuItem;
	private JRadioButtonMenuItem bfsMenuItem;
	private boolean randomized = false;
	private Vertex start = null;
	private Deque<OrientedEdge> deque;

	public FSAnimator(InteractiveBoard board) {
		super();
		this.board = board;
		board.addGameModeListener(new ModeListener());
		updateControl();
		board.addTouchListener(new TouchAdapter() {
			@Override
			public void touchStarted(TouchEvent e) {
				synchronized (deque) {
					start = e.getVertex();
					deque.add(new OrientedEdge(null, start));
				}
			}
		});
	}

	@Override
	public AnimatorFactory getFactory() {
		return FSAnimatorFactory.getFactory();
	}

	/**
	 * @return whether edges are queued in a specific order or randomly
	 */
	public boolean isRandomized() {
		return randomized;
	}

	@Override
	public void setMenu(JMenu menu) {
		super.setMenu(menu);
		menu.add(randomizedMenuItem);
		menu.add(dfsMenuItem);
		menu.add(bfsMenuItem);
	}

	/**
	 * Sets whether edges are queued in a specific order or randomly
	 * 
	 * @param randomized
	 */
	public void setRandomized(boolean randomized) {
		this.randomized = randomized;
	}

	@Override
	protected void clean() {
		board.getBoard().getGraph().unuseAll();
		board.repaint();
	}

	@Override
	protected void init() {
		start = null;
		board.getBoard().getGraph().unuseAll();
		deque = new LinkedList<FSAnimator.OrientedEdge>();
		board.repaint();
	}

	@Override
	protected void makeControl() {
		super.makeControl();
		randomizedMenuItem = new JCheckBoxMenuItem(new RandomizedAction());
		setRandomized(randomizedMenuItem.isSelected());
		ButtonGroup fsGroup = new ButtonGroup();
		dfsMenuItem = new JRadioButtonMenuItem(new DFSAction());
		dfsMenuItem.setSelected(true);
		fs = FS.DFS;
		fsGroup.add(dfsMenuItem);
		bfsMenuItem = new JRadioButtonMenuItem(new BFSAction());
		bfsMenuItem.setSelected(false);
		fsGroup.add(bfsMenuItem);
	}

	@Override
	protected boolean step() {
		if (start == null) {
			if (board.isTouchActive()) {

			} else {
				board.setTouchActive(true);
			}
			return false;
		}
		if (board.isTouchActive()) {
			board.setTouchActive(false);
		}
		synchronized (deque) {
			do {
				if (deque.isEmpty()) {
					return true;
				}
				OrientedEdge oe = fs.removeEdge(deque);
				Vertex v;
				if (oe.getEdge() == null) {
					v = oe.getFrom();
				} else {
					v = oe.getTo();
					Edge e = oe.getEdge();
					if (e.isUsed()) {
						continue;
					}
					e.setUsed(true);
					board.repaintEdge(e);
				}
				Iterator<Edge> i = v.edgesIterator(true);
				deque.addAll(Order.getOrder(randomized).convertIterator(v, i));
				break;
			} while (true);
			return deque.isEmpty();
		}
	}

	@Override
	protected void updateControl() {
		super.updateControl();
		GameMode gm = board == null ? GameMode.SHOW : board.getMode();
		if (gm.equals(GameMode.RUN)) {
			stepSlider.setEnabled(true);
		} else {
			stepSlider.setEnabled(false);
			playPauseButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
		boolean b = isStopped() && gm.equals(GameMode.RUN);
		randomizedMenuItem.setEnabled(b);
		dfsMenuItem.setEnabled(b);
		bfsMenuItem.setEnabled(b);
	}

}
