package pf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.animator.AnimatorFactory;
import pf.animator.Animators;
import pf.board.AbstractGrid;
import pf.board.Board;
import pf.board.BoardImpl;
import pf.board.BoardImpl.GridForm;
import pf.board.Grid;
import pf.board.GridPattern;
import pf.board.GridType;
import pf.interactive.GameBoard;
import pf.interactive.GameMode;
import pf.interactive.GridPainterImpl;
import pf.interactive.VerticesPainterImpl;
import pf.interactive.VerticesPainterImpl.DegreeType;

import net.miginfocom.swing.MigLayout;

/**
 * Creates a new board either from file or hand input. This one has two cards.
 * 
 * @author Adam Juraszek
 * 
 */
public class NewDialog extends CardDialog {
	private static final long serialVersionUID = 1L;

	public static final String title = "New dialog";

	private File file = new File("");
	private BoardImpl.GridForm form;
	private int type;

	private GameBoard gb;

	private JSpinner brptf3y;

	private JSpinner brptf3x;

	private JSpinner brptf2y;

	private JSpinner brptf2x;

	private JSpinner brptf1y;

	private JSpinner brptf1x;

	private JRadioButton brrb2;

	private JRadioButton brrb1;

	private JRadioButton patrb2;

	private JRadioButton patrb1;

	private JComboBox blcb;

	private Map<GridType, Integer> types;

	private JSpinner whtfw;

	private JSpinner whtfh;
	private JButton brpb;
	private JButton whb;

	private JComboBox anicb;
	private JComboBox patcb;
	private JCheckBox modechb;
	private JButton saveb;

	private boolean userInput = false;

	private JComboBox modecb;

	private JLabel patl;

	public NewDialog(JFrame owner) {
		super(owner, title);
		userInput = true;
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	public AnimatorFactory getAnimator() {
		return (AnimatorFactory) anicb.getSelectedItem();
	}

	public Board getBoard() throws FileNotFoundException {
		Board b;
		if (file != null) {
			if (patrb1.isSelected()) {
				b = BoardImpl.createBoard(file, null);
			} else {
				b = BoardImpl.createBoard(file, getPattern());
			}
		} else {
			b = new BoardImpl(gb.getBoard().getGrid(),
					gb.getBoard().getWidth(), gb.getBoard().getHeight(),
					getPattern());
		}
		return b;
	}

	public GameMode getMode() {
		return (GameMode) modecb.getSelectedItem();
	}

	public GridPattern getPattern() {
		if (patrb1.isSelected()) {
			return null;
		}
		return (GridPattern) patcb.getSelectedItem();
	}

	public boolean isEditAllowed() {
		return modechb.isSelected();
	}

	public boolean isFilePattern() {
		return patrb1.isSelected();
	}

	private JPanel makeCard1() {
		JPanel card = new JPanel(new MigLayout("", "fill,grow",
				"[][grow,fill][]"));

		JPanel top = new JPanel(new MigLayout("", "[30%] [grow,50%,right] []"));
		top.setBorder(BorderFactory.createTitledBorder("Source"));
		ButtonGroup topbg = new ButtonGroup();
		JRadioButton toprb1 = new JRadioButton("Load from file", true);
		topbg.add(toprb1);
		top.add(toprb1);
		final JTextField toptf = new JTextField(20);
		toptf.setEditable(false);
		top.add(toptf, "grow");
		final JButton topb = new JButton("Browse");
		top.add(topb, "wrap");

		JRadioButton toprb2 = new JRadioButton("Custom", false);
		topbg.add(toprb2);
		top.add(toprb2);

		card.add(top, "wrap,span 2,grow");

		JPanel bl = new JPanel(new MigLayout("", "grow", "[] [grow,fill]"));
		bl.setBorder(BorderFactory.createTitledBorder("GridType"));
		blcb = new JComboBox();
		int index = 0;
		for (GridType gt : GridType.values()) {
			blcb.addItem(gt);
			types.put(gt, index++);
		}
		blcb.setEnabled(false);
		blcb.setSelectedIndex(type);
		bl.add(blcb, "wrap");

		gb = new GameBoard();
		bl.add(gb, "grow");
		card.add(bl, "spany 2");

		JPanel wh = new JPanel(new MigLayout("", "[right] [grow,fill]"));
		wh.setBorder(BorderFactory.createTitledBorder("Size"));
		JLabel whlw = new JLabel("width");
		wh.add(whlw);
		whtfw = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		whtfw.setEnabled(false);
		wh.add(whtfw, "wrap");
		JLabel whlh = new JLabel("height");
		wh.add(whlh);
		whtfh = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		whtfh.setEnabled(false);
		wh.add(whtfh, "wrap");
		whb = new JButton("Refresh");
		wh.add(whb, "span 2");
		whb.setEnabled(false);

		card.add(wh, "wrap");

		JPanel br = new JPanel(new MigLayout("", "grow,fill"));
		br.setBorder(BorderFactory.createTitledBorder("Grid Form"));

		ButtonGroup brbg = new ButtonGroup();
		brrb1 = new JRadioButton("Regular", true);
		brbg.add(brrb1);
		brrb1.setEnabled(false);
		br.add(brrb1, "wrap");
		brrb2 = new JRadioButton("Free", false);
		brbg.add(brrb2);
		brrb2.setEnabled(false);
		br.add(brrb2, "wrap");

		br.add(new JSeparator(), "wrap,grow");

		JPanel brp = new JPanel(new MigLayout("",
				"[right] [grow,fill] [grow,fill]"));
		brp.setBorder(BorderFactory.createTitledBorder("Points"));

		JLabel brplp = new JLabel("Point");
		brp.add(brplp);
		JLabel brplx = new JLabel("x");
		brp.add(brplx);
		JLabel brply = new JLabel("y");
		brp.add(brply, "wrap");

		JLabel brplp1 = new JLabel("P1");
		brp.add(brplp1);
		brptf1x = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1));
		brptf1x.setEnabled(false);
		brptf1y = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1));
		brptf1y.setEnabled(false);
		brp.add(brptf1x);
		brp.add(brptf1y, "wrap");

		JLabel brplp2 = new JLabel("P2");
		brp.add(brplp2);
		brptf2x = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1));
		brptf2x.setEnabled(false);
		brptf2y = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1));
		brptf2y.setEnabled(false);
		brp.add(brptf2x);
		brp.add(brptf2y, "wrap");

		JLabel brplp3 = new JLabel("P3");
		brp.add(brplp3);
		brptf3x = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1));
		brptf3x.setEnabled(false);
		brptf3y = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE,
				Integer.MAX_VALUE, 1));
		brptf3y.setEnabled(false);
		brp.add(brptf3x);
		brp.add(brptf3y, "wrap");

		brpb = new JButton("Refresh");
		brp.add(brpb, "span 3");
		brpb.setEnabled(false);

		br.add(brp);
		card.add(br, "grow, wrap");

		JPanel save = new JPanel(new MigLayout("right"));
		save.add(new JLabel("Save all options specified above to file"), "grow");
		saveb = new JButton("Save");
		saveb.setEnabled(false);
		saveb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveDialog s = new SaveDialog(NewDialog.this, gb.getBoard());
				s.setVisible(true);
			}
		});
		save.add(saveb);

		card.add(save, "grow, span 2");

		// ##############################

		toprb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!userInput) {
					return;
				}
				userInput = false;
				file = new File("");
				toptf.setText("");
				topb.setEnabled(true);
				setBottomEnabled(false);

				updateGraph();
				updateButtons();
				userInput = true;
			}
		});
		toprb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!userInput) {
					return;
				}
				userInput = false;
				file = null;
				toptf.setText("");
				topb.setEnabled(false);
				setBottomEnabled(true);
				setGridType((GridType) blcb.getSelectedItem());

				updateGraph();
				updateButtons();
				userInput = true;
			}
		});
		topb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!userInput) {
					return;
				}
				userInput = false;
				JFileChooser fc = new JFileChooser();
				int status = fc.showOpenDialog(NewDialog.this);
				if (status == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					if (f.exists()) {

						file = f;
						toptf.setText(file.getPath());
						updateGraph();
						updateButtons();
					}
				}
				userInput = true;
			}
		});

		// ##############################

		blcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!userInput) {
					return;
				}
				userInput = false;
				setGridType((GridType) blcb.getSelectedItem());
				updateGraph();
				updateButtons();
				userInput = true;
			}
		});

		brrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!userInput) {
					return;
				}
				userInput = false;
				form = GridForm.REGULAR;
				setPointsEnabled(false);
				setGridType((GridType) blcb.getSelectedItem());

				updateGraph();
				updateButtons();
				userInput = true;
			}
		});
		brrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				form = GridForm.FREE;
				setPointsEnabled(true);

				updateGraph();
				updateButtons();
			}
		});
		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!userInput) {
					return;
				}
				userInput = false;
				updateGraph();
				updateButtons();
				userInput = true;
			}
		};

		whb.addActionListener(update);
		brpb.addActionListener(update);
		return card;
	}

	private JPanel makeCard2() {
		JPanel card = new JPanel(new MigLayout("", "fill, grow"));

		JPanel pat = new JPanel(new MigLayout("", "fill, grow"));
		pat.setBorder(BorderFactory.createTitledBorder("Pattern"));

		ButtonGroup patbr = new ButtonGroup();
		patrb1 = new JRadioButton("From file", true);
		patbr.add(patrb1);
		patrb1.setEnabled(false);
		pat.add(patrb1);
		patl = new JLabel();
		pat.add(patl, "wrap");
		patrb2 = new JRadioButton("Predefined", false);
		patbr.add(patrb2);
		patrb2.setEnabled(false);
		pat.add(patrb2);

		patcb = new JComboBox();
		for (GridPattern gp : GridPattern.values()) {
			if (gp.isSimple() && !gp.isInternal()) {
				patcb.addItem(gp);
			}
		}
		pat.add(patcb);

		card.add(pat, "grow, wrap");

		JPanel mode = new JPanel(new MigLayout("", "fill, grow"));
		mode.setBorder(BorderFactory.createTitledBorder("Mode"));

		modechb = new JCheckBox("Allow editing");
		modechb.setSelected(true);
		mode.add(modechb, "wrap, span 2");

		JLabel model = new JLabel("Startup mode");
		mode.add(model);

		modecb = new JComboBox();
		modecb.addItem(GameMode.SHOW);
		modecb.setSelectedItem(GameMode.SHOW);
		modecb.addItem(GameMode.EDIT);
		mode.add(modecb);

		card.add(mode, "grow, wrap");

		JPanel ani = new JPanel(new MigLayout("", "fill, grow"));
		ani.setBorder(BorderFactory.createTitledBorder("Animator"));

		anicb = new JComboBox();
		anicb.addItem(null);
		for (AnimatorFactory aa : Animators.getInstance().getAnimators()) {
			anicb.addItem(aa);
		}
		anicb.setSelectedItem(null);
		ani.add(anicb, "grow");

		card.add(ani, "grow");

		patrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patcb.setEnabled(false);
				setPatLabel();
				patl.setEnabled(true);
			}
		});

		patrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patcb.setEnabled(true);
				setPatLabel();
				patl.setEnabled(false);
			}
		});

		// ##############################

		modechb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!modechb.isSelected()) {
					if (modecb.getSelectedItem().equals(GameMode.EDIT)) {
						modecb.setSelectedItem(GameMode.SHOW);
					}
					modecb.removeItem(GameMode.EDIT);
				} else {
					modecb.addItem(GameMode.EDIT);
				}
			}
		});

		anicb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (anicb.getSelectedItem() == null) {
					if (modecb.getSelectedItem().equals(GameMode.RUN)) {
						modecb.setSelectedItem(GameMode.SHOW);
					}
					modecb.removeItem(GameMode.RUN);
				} else {
					modecb.addItem(GameMode.RUN);
				}
			}
		});

		return card;
	}

	private void setBottomEnabled(boolean b) {
		blcb.setEnabled(b);
		brrb1.setEnabled(b);
		brrb2.setEnabled(b);
		whtfh.setEnabled(b);
		whtfw.setEnabled(b);
		whb.setEnabled(b);
		if (b && form.equals(GridForm.FREE)) {
			setPointsEnabled(true);
		} else {
			setPointsEnabled(false);
		}
	}

	private void setGridType(GridType gt) {
		blcb.setSelectedIndex(types.get(gt));
		if (form.equals(GridForm.REGULAR)) {
			Point[] points = gt.getRegularPoints();
			brptf1x.setValue(points[0].getX());
			brptf1y.setValue(points[0].getY());
			brptf2x.setValue(points[1].getX());
			brptf2y.setValue(points[1].getY());
			brptf3x.setValue(points[2].getX());
			brptf3y.setValue(points[2].getY());
		}
	}

	private void setPatLabel() {
		if (file == null) {
			patl.setText("");
		} else {
			try {
				BoardImpl.FileHeader fh = new BoardImpl.FileHeader(file);
				GridPattern gp = fh.pattern;
				patl.setText(gp.toString());
			} catch (Exception ex) {
				patl.setText("Error");
			}
		}
	}

	private void setPointsEnabled(boolean b) {
		brptf1x.setEnabled(b);
		brptf1y.setEnabled(b);
		brptf2x.setEnabled(b);
		brptf2y.setEnabled(b);
		brptf3x.setEnabled(b);
		brptf3y.setEnabled(b);
		brpb.setEnabled(b);
	}

	private void updateGraph() {
		Board b = null;
		if (file != null) {
			try {
				b = BoardImpl.createBoard(file);
				gb.setBoard(b);
			} catch (Exception ex) {
				b = null;
				file = new File("");
			}
			if (b != null) {
				Point[] points = b.getGrid().getPoints();
				brptf1x.setValue(points[0].getX());
				brptf1y.setValue(points[0].getY());
				brptf2x.setValue(points[1].getX());
				brptf2y.setValue(points[1].getY());
				brptf3x.setValue(points[2].getX());
				brptf3y.setValue(points[2].getY());
				if (b.getGrid().getGridType().isRegular(points)) {
					form = GridForm.REGULAR;
					brrb1.setSelected(true);
					brrb2.setSelected(false);
				} else {
					form = GridForm.FREE;
					brrb1.setSelected(false);
					brrb2.setSelected(true);
				}
				type = types.get(b.getGrid().getGridType());
				blcb.setSelectedIndex(type);
				whtfw.setValue(b.getWidth());
				whtfh.setValue(b.getHeight());
			}
		} else {
			try {
				Point p1 = new PointImpl(
						((Number) brptf1x.getValue()).intValue(),
						((Number) brptf1y.getValue()).intValue());
				Point p2 = new PointImpl(
						((Number) brptf2x.getValue()).intValue(),
						((Number) brptf2y.getValue()).intValue());
				Point p3 = new PointImpl(
						((Number) brptf3x.getValue()).intValue(),
						((Number) brptf3y.getValue()).intValue());
				Grid g = AbstractGrid.createGrid(
						(GridType) blcb.getSelectedItem(), p1, p2, p3);
				b = new BoardImpl(g, ((Number) whtfw.getValue()).intValue(),
						((Number) whtfh.getValue()).intValue());
			} catch (Exception ex) {
				b = null;
			}
		}

		if (b != null) {
			gb.setBoard(b);
			gb.setGridPainterAndPaint(new GridPainterImpl((GridType) blcb
					.getSelectedItem()));
			gb.setVerticesPainterAndPaint(new VerticesPainterImpl(
					(GridType) blcb.getSelectedItem(), DegreeType.BY_ALL));
		} else {
			gb.setBoard(null);
			gb.setGridPainter(null);
			gb.setVerticesPainter(null);
		}
	}

	@Override
	protected boolean canFinish() {
		return getCurrent() == 1 && canNext();
	}

	@Override
	protected boolean canNext() {
		return gb.getBoard() != null;
	}

	@Override
	protected boolean canPrev() {
		return true;
	}

	@Override
	protected void flipNext() {
		if (file == null) {
			patrb1.setEnabled(false);
			patrb1.setSelected(false);
			patrb2.setEnabled(false);
			patcb.setEnabled(true);
			patrb2.setSelected(true);
			patl.setEnabled(false);
			patl.setText("");
		} else {
			patrb1.setEnabled(true);
			patrb2.setEnabled(true);
			setPatLabel();
			if (patrb1.isSelected()) {
				patcb.setEnabled(false);
				patl.setEnabled(true);
			} else {
				patcb.setEnabled(true);
				patl.setEnabled(false);
			}
		}
	}

	@Override
	protected void flipPrev() {
	}

	@Override
	protected void makeContent() {
		types = new HashMap<GridType, Integer>();
		type = 0;
		form = GridForm.REGULAR;
		addCard(makeCard1());
		addCard(makeCard2());
	}

	@Override
	protected void updateButtons() {
		super.updateButtons();
		saveb.setEnabled(gb.getBoard() != null);
	}
}
