package pf.animator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import pf.graph.Path;
import pf.gui.PathPainterDialog;
import pf.interactive.GameMode;
import pf.interactive.GameModeEvent;
import pf.interactive.GameModeListener;
import pf.interactive.InteractiveBoard;
import pf.interactive.PathPainterImpl;

/**
 * @author Adam Juraszek
 * 
 */
public class EulerAnimator extends StepAnimator {

	/**
	 * 
	 * Factory for {@link EulerAnimator}
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class EulerAnimatorFactory implements AnimatorFactory {

		private static volatile EulerAnimatorFactory factory = null;

		public static EulerAnimatorFactory getFactory() {
			if (factory == null) {
				synchronized (EulerAnimatorFactory.class) {
					if (factory == null) {
						factory = new EulerAnimatorFactory();
					}
				}
			}
			return factory;
		}

		private EulerAnimatorFactory() {
		}

		@Override
		public Animator newInstance(InteractiveBoard board) {
			return new EulerAnimator(board);
		}

		@Override
		public String toString() {
			return "Animator draws the lowest number of paths to use each edge";
		}

	}

	/**
	 * Listens to mode changes of {@link InteractiveBoard} to set enabled menu
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
	 * Allows to select whether animate all paths simultaneously.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected class SimultaneousAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SimultaneousAction() {
			super("Simultaneously");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setSimultaneous(simultaneousMenuItem.isSelected());
		}
	}

	/**
	 * Shows dialog in which user can change the way how paths are painted.
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	class PathAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public PathAction() {
			super("Path painters");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int pc = EulerPaths.getPathsCount(getBoard().getBoard().getGraph());
			makePathPainters(pc);
			PathPainterDialog ppd = new PathPainterDialog(null, pathPaintes);
			ppd.setVisible(true);
			if (ppd.isClosedProperly()) {
				for (int i = 0; i < pathPaintes.size(); i++) {
					pathPaintes.get(i).set(ppd.getPathPainters().get(i));
					getBoard().repaint();
				}
			}
		}
	}

	private final InteractiveBoard board;

	protected List<PartialPath> paths;

	protected List<PathPainterImpl> pathPaintes;

	private JMenuItem pathMenuItem;

	private JCheckBoxMenuItem simultaneousMenuItem;

	private boolean simultaneous = false;

	static {
		Animators.getInstance().addAnimator(EulerAnimatorFactory.getFactory());
	}

	public EulerAnimator(InteractiveBoard board) {
		super();
		this.board = board;
		board.addGameModeListener(new ModeListener());
		paths = new ArrayList<PartialPath>();
		pathPaintes = new ArrayList<PathPainterImpl>();
		updateControl();
	}

	public InteractiveBoard getBoard() {
		return board;
	}

	@Override
	public AnimatorFactory getFactory() {
		return EulerAnimatorFactory.getFactory();
	}

	/**
	 * @return whether animate all paths simultaneously
	 */
	public boolean isSimultaneous() {
		return simultaneous;
	}

	@Override
	public void setMenu(JMenu menu) {
		super.setMenu(menu);
		menu.add(pathMenuItem);
		menu.add(simultaneousMenuItem);
	}

	/**
	 * Sets whether animate all paths simultaneously
	 * 
	 * @param simultaneous
	 */
	public void setSimultaneous(boolean simultaneous) {
		this.simultaneous = simultaneous;
	}

	@Override
	protected void clean() {
		board.removePaths();
		board.getBoard().getGraph().unuseAll();
		board.repaint();
	}

	@Override
	protected void init() {
		getBoard().removePaths();
		makePaths();
		getBoard().repaint();
	}

	@Override
	protected void makeControl() {
		super.makeControl();
		pathMenuItem = new JMenuItem(new PathAction());
		simultaneousMenuItem = new JCheckBoxMenuItem(new SimultaneousAction());
	}

	/**
	 * Prepares path painters.
	 * 
	 * @param pc
	 */
	protected void makePathPainters(int pc) {
		if (pathPaintes.size() < pc) {
			for (int i = pathPaintes.size(); i <= pc; i++) {
				pathPaintes.add(new PathPainterImpl(getBoard()));
			}
		} else if (pathPaintes.size() > pc) {
			for (int i = pathPaintes.size() - 1; i >= pc; i--) {
				pathPaintes.remove(i);
			}
		}
	}

	/**
	 * Calculates paths to be animated.
	 */
	protected void makePaths() {
		paths.clear();
		List<Path> ppaths = EulerPaths.getEulerPaths(getBoard().getBoard()
				.getGraph());
		makePathPainters(ppaths.size());
		for (int i = 0; i < ppaths.size(); i++) {
			PartialPath pp = new PartialPath(ppaths.get(i));
			pp.setLength(0);
			paths.add(pp);
			board.addPath(pp);
			PathPainterImpl ppa = pathPaintes.get(i);
			ppa.setPath(pp);
			board.setPathPainter(pp, ppa);
		}
		board.setPaintPaths(true);
	}

	@Override
	protected boolean step() {
		boolean finished = true;
		for (PartialPath pp : paths) {
			if (pp.length() < pp.realLength()) {
				pp.setLength(pp.length() + 1);
				finished = false;
				if (!isSimultaneous()) {
					break;
				}
			}
		}
		board.repaint();
		return finished;
	}

	@Override
	protected void updateControl() {
		super.updateControl();
		GameMode gm = board == null ? GameMode.SHOW : board.getMode();
		if (gm.equals(GameMode.RUN)) {
			if (isRunning()) {
				pathMenuItem.setEnabled(false);
			} else {
				pathMenuItem.setEnabled(true);
			}
			if (isStopped()) {
				simultaneousMenuItem.setEnabled(true);
			} else {
				simultaneousMenuItem.setEnabled(false);
			}
			stepSlider.setEnabled(true);
		} else {
			stepSlider.setEnabled(false);
			playPauseButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
	}
}
