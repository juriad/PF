package pf.animator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import pf.graph.Path;
import pf.gui.PathPainterDialog;
import pf.interactive.InteractiveBoard;
import pf.interactive.PathPainterImpl;

public abstract class EulerAnimator extends StepAnimator {

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
			int pc = EulerPaths.getPathsCount(board.getBoard().getGraph());
			if (pathPaintes.size() < pc) {
				for (int i = pathPaintes.size(); i <= pc; i++) {
					pathPaintes.add(new PathPainterImpl(board));
				}
			} else if (pathPaintes.size() > pc) {
				for (int i = pathPaintes.size() - 1; i >= pc; i--) {
					pathPaintes.remove(i);
				}
			}
			PathPainterDialog ppd = new PathPainterDialog(null, pathPaintes);
			ppd.setVisible(true);
			if (ppd.isClosedProperly()) {
				for (int i = 0; i < pathPaintes.size(); i++) {
					pathPaintes.get(i).set(ppd.getPathPainters().get(i));
					board.repaint();
				}
			}
		}
	}

	private final InteractiveBoard board;

	protected List<PartialPath> paths;

	protected List<PathPainterImpl> pathPaintes;

	private JMenuItem pathMenuItem;

	public EulerAnimator(InteractiveBoard board) {
		super();
		this.board = board;
		paths = new ArrayList<PartialPath>();
		pathPaintes = new ArrayList<PathPainterImpl>();
	}

	@Override
	public void setMenu(JMenu menu) {
		super.setMenu(menu);
		pathMenuItem = new JMenuItem(new PathAction());
		menu.add(pathMenuItem);
	}

	@Override
	protected void init() {
		board.removePaths();
		makePaths();
		board.repaint();
	}

	protected void makePaths() {
		for (Path p : EulerPaths.getEulerPaths(board.getBoard().getGraph())) {
			paths.add(new PartialPath(p));
		}
	}

	@Override
	protected boolean step() {
		// TODO step
		return false;
	}

	@Override
	protected void updateControl() {
		super.updateControl();
		if (isRunning()) {
			pathMenuItem.setEnabled(false);
		} else {
			pathMenuItem.setEnabled(true);
		}
	}

}
