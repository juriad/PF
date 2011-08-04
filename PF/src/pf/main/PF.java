package pf.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import pf.animator.AnimatorFactory;
import pf.board.Board;
import pf.board.GridType;
import pf.gui.AnimatorDialog;
import pf.gui.EdgesPainterDialog;
import pf.gui.GridPainterDialog;
import pf.gui.InteractiveBoardControl;
import pf.gui.NewDialog;
import pf.gui.SaveDialog;
import pf.gui.VerticesPainterDialog;
import pf.interactive.EdgesPainterImpl;
import pf.interactive.GameMode;
import pf.interactive.GameModeEvent;
import pf.interactive.GameModeListener;
import pf.interactive.GridPainterImpl;
import pf.interactive.InteractiveBoard;
import pf.interactive.TouchEvent;
import pf.interactive.TouchListener;
import pf.interactive.VerticesPainterImpl;
import pf.interactive.VerticesPainterImpl.DegreeType;

public class PF extends JFrame {
	public class TListener implements TouchListener {

		@Override
		public void touchCancelled(TouchEvent e) {
			if (!board.getMode().equals(GameMode.EDIT)) {
				return;
			}
			System.out.println("t cancelled");
			// do nothing

		}

		@Override
		public void touchEnded(TouchEvent e) {
			if (!board.getMode().equals(GameMode.EDIT)) {
				return;
			}
			System.out.println("t ended");
			// do nothing
		}

		@Override
		public void touchLonger(TouchEvent e) {
			if (!board.getMode().equals(GameMode.EDIT)) {
				return;
			}
			System.out.println("t longer");
			// toggle used
			e.getEdge().setUsed(!e.getEdge().isUsed());
			board.repaintEdge(e.getEdge());
		}

		@Override
		public void touchShorter(TouchEvent e) {
			if (!board.getMode().equals(GameMode.EDIT)) {
				return;
			}
			System.out.println("t shorter");
			// toggle used
			e.getEdge().setUsed(!e.getEdge().isUsed());
			board.repaintEdge(e.getEdge());
		}

		@Override
		public void touchStarted(TouchEvent e) {
			if (!board.getMode().equals(GameMode.EDIT)) {
				return;
			}
			System.out.println("t started");
			// do nothing
		}

	}

	protected class ModeListener implements GameModeListener {

		@Override
		public void modeEdit(GameModeEvent e) {
			updatePainters();
			board.setTouchActive(true);
			board.setTouchReturnAllowed(true);
			setAnimatorControlEnabled(false);
		}

		@Override
		public void modeRun(GameModeEvent e) {
			updatePainters();
			setAnimatorControlEnabled(true);
		}

		@Override
		public void modeShow(GameModeEvent e) {
			updatePainters();
			setAnimatorControlEnabled(false);
		}
	}

	protected class Painters {
		GridPainterImpl gpe, gps, gpr;
		EdgesPainterImpl epe, eps, epr;
		VerticesPainterImpl vpe, vps, vpr;

		public Painters(GridType gt, DegreeType degreeType) {
			gpe = gps = gpr = new GridPainterImpl(gt);
			epe = eps = epr = new EdgesPainterImpl();
			vpe = vps = vpr = new VerticesPainterImpl(gt, degreeType);
		}
	}

	class AnimatorAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public AnimatorAction() {
			super("Animators");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			AnimatorDialog ad = new AnimatorDialog(PF.this);
			ad.setVisible(true);
			if (ad.isClosedProperly()) {
				if (ad.getAnimator() == null
						&& board.getMode().equals(GameMode.RUN)) {
					board.setMode(GameMode.SHOW);
				}
				setAnimator(ad.getAnimator());
				ibc.setButtons();
			}
		}
	}

	class EdgeAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public EdgeAction() {
			super("Edges painters");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			EdgesPainterDialog epd = new EdgesPainterDialog(PF.this,
					board.isEditable(), painters.epe, true, painters.eps,
					board.getAnimator() != null, painters.epr);
			epd.setVisible(true);
			if (epd.isClosedProperly()) {
				painters.epe = epd.getEditEdgesPainter();
				painters.eps = epd.getShowEdgesPainter();
				painters.epr = epd.getRunEdgesPainter();
				updatePainters();
			}
		}
	}

	class GridAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GridAction() {
			super("Grid painters");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			GridPainterDialog gpd = new GridPainterDialog(PF.this,
					board.isEditable(), painters.gpe, true, painters.gps,
					board.getAnimator() != null, painters.gpr, board.getBoard()
							.getGrid().getGridType());
			gpd.setVisible(true);
			if (gpd.isClosedProperly()) {
				painters.gpe = gpd.getEditGridPainter();
				painters.gps = gpd.getShowGridPainter();
				painters.gpr = gpd.getRunGridPainter();
				updatePainters();
			}
		}
	}

	class NewAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public NewAction() {
			super("New");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			NewDialog nd = new NewDialog(PF.this);
			nd.setVisible(true);
			Board b;
			try {
				if (nd.isClosedProperly() && (b = nd.getBoard()) != null) {
					board.setBoard(b);
					board.setEditable(nd.isEditAllowed());
					if (nd.getAnimator() != null) {
						setAnimator(nd.getAnimator());
					}
					ibc.setButtons();
					painters = new Painters(board.getBoard().getGrid()
							.getGridType(), DegreeType.BY_UNUSED);
					board.setMode(nd.getMode());
					updatePainters();
					updateMenu();
				}
			} catch (FileNotFoundException ex) {
				JOptionPane.showMessageDialog(getOwner(),
						"A problem occured during opening board from file",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class QuitAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public QuitAction() {
			super("Quit");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			quit();
		}
	}

	class SaveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SaveAction() {
			super("Save");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			SaveDialog sd = new SaveDialog(PF.this, board.getBoard());
			sd.setVisible(true);
		}
	}

	class VertexAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public VertexAction() {
			super("Vertices painters");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			VerticesPainterDialog vpd = new VerticesPainterDialog(PF.this,
					board.isEditable(), painters.vpe, true, painters.vps,
					board.getAnimator() != null, painters.vpr, board.getBoard()
							.getGrid().getGridType());
			vpd.setVisible(true);
			if (vpd.isClosedProperly()) {
				painters.vpe = vpd.getEditVerticesPainter();
				painters.vps = vpd.getShowVerticesPainter();
				painters.vpr = vpd.getRunVerticesPainter();
				updatePainters();
			}
		}
	}

	private static final long serialVersionUID = 1L;

	private static final String title = "PFko";

	public static void main(String[] args) {
		new PF().setVisible(true);
	}

	private Painters painters;

	private final BorderLayout layout;

	private final InteractiveBoard board;

	private JMenuItem bms;

	private JMenuItem pmg;

	private JMenuItem pme;
	private JMenuItem pmv;
	private InteractiveBoardControl ibc;
	private JMenu amenu;

	public PF() {
		super(title);

		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);

		JMenu bmenu = new JMenu("Board");
		bar.add(bmenu);
		JMenu pmenu = new JMenu("Painters");
		bar.add(pmenu);
		amenu = new JMenu("Animator");
		bar.add(amenu);
		JMenu hmenu = new JMenu("Help");
		bar.add(hmenu);

		bmenu.setMnemonic(KeyEvent.VK_B);
		JMenuItem bmn = new JMenuItem(new NewAction());
		bmenu.add(bmn);
		bms = new JMenuItem(new SaveAction());
		bmenu.add(bms);

		JMenuItem bmq = new JMenuItem(new QuitAction());
		bmenu.add(bmq);

		pmenu.setMnemonic(KeyEvent.VK_P);
		pmg = new JMenuItem(new GridAction());
		pmenu.add(pmg);
		pme = new JMenuItem(new EdgeAction());
		pmenu.add(pme);
		pmv = new JMenuItem(new VertexAction());
		pmenu.add(pmv);

		amenu.setMnemonic(KeyEvent.VK_A);

		Container pane = getContentPane();
		pane.setLayout(layout = new BorderLayout());

		board = new InteractiveBoard();
		board.setPreferredSize(new Dimension(200, 200));
		board.addGameModeListener(new ModeListener());
		board.addTouchListener(new TListener());
		pane.add(board, BorderLayout.CENTER);

		pane.add(ibc = new InteractiveBoardControl(board), BorderLayout.NORTH);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});

		updateMenu();

		addons();
		pack();
	}

	public void setAnimator(AnimatorFactory af) {
		board.setAnimator(af != null ? af.newInstance(board) : null);
		if (board.getAnimator() != null) {
			setAnimatorControl(board.getAnimator().getAnimatorControl());
		} else {
			setAnimatorControl(null);
		}
		setAnimatorMenu();
	}

	public void updateMenu() {
		boolean b = board.getBoard() != null;
		bms.setEnabled(b);
		pmg.setEnabled(b);
		pme.setEnabled(b);
		pmv.setEnabled(b);
		setAnimatorMenu();
	}

	private void addons() {
		try {
			Class.forName("pf.animator.EulerAnimator");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void quit() {
		System.exit(0);
	}

	private void setAnimatorControl(Component ac) {
		Component c = layout.getLayoutComponent(BorderLayout.SOUTH);
		if (c != null) {
			getContentPane().remove(c);
		}
		if (ac != null) {
			getContentPane().add(ac, BorderLayout.SOUTH);
		}
		validate();
		repaint();
	}

	private void setAnimatorControlEnabled(boolean b) {
		Component c = layout.getLayoutComponent(BorderLayout.SOUTH);
		if (c != null) {
			c.setEnabled(b);
		}
	}

	private void setAnimatorMenu() {
		amenu.removeAll();
		if (board.getBoard() != null) {
			amenu.add(new AnimatorAction());
		}
		if (board.getAnimator() != null) {
			amenu.addSeparator();
			board.getAnimator().setMenu(amenu);
		}
	}

	private void updatePainters() {
		switch (board.getMode()) {
		case EDIT:
			board.setGridPainterAndPaint(painters.gpe);
			board.setEdgesPainterAndPaint(painters.epe);
			board.setVerticesPainterAndPaint(painters.vpe);
			break;
		case SHOW:
			board.setGridPainterAndPaint(painters.gps);
			board.setEdgesPainterAndPaint(painters.eps);
			board.setVerticesPainterAndPaint(painters.vps);
			break;
		case RUN:
			board.setGridPainterAndPaint(painters.gpr);
			board.setEdgesPainterAndPaint(painters.epr);
			board.setVerticesPainterAndPaint(painters.vpr);
			break;
		}
		board.repaint();
	}
}
