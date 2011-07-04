package pf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.board.AbstractGrid;
import pf.board.Board;
import pf.board.BoardImpl;
import pf.board.BoardImpl.GridForm;
import pf.board.Grid;
import pf.board.GridPattern;
import pf.board.GridType;
import pf.interactive.AbstractAnimator;
import pf.interactive.AnimatorFactory;
import pf.interactive.GameBoard;
import pf.interactive.GridPainterImpl;
import pf.interactive.VerticesPainterImpl;
import pf.interactive.VerticesPainterImpl.DegreeType;

import net.miginfocom.swing.MigLayout;

public class NewDialog extends CardDialog {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new NewDialog(null).setVisible(true);
	}

	private File file = new File("");
	private BoardImpl.GridForm form;
	private int type;

	private GameBoard gb;

	private JFormattedTextField brptf3y;

	private JFormattedTextField brptf3x;

	private JFormattedTextField brptf2y;

	private JFormattedTextField brptf2x;

	private JFormattedTextField brptf1y;

	private JFormattedTextField brtpf1x;

	private JRadioButton brrb2;

	private JRadioButton brrb1;

	private JRadioButton patrb2;

	private JRadioButton patrb1;

	private JComboBox blcb;

	private Map<GridType, Integer> types;

	private JFormattedTextField whtfw;

	private JFormattedTextField whtfh;
	private JButton brpb;
	private JButton whb;

	private JComboBox anicb;
	private JComboBox patcb;
	private JCheckBox modecb;

	private boolean userInput = false;

	public NewDialog(JFrame owner) {
		super(owner);
		userInput = true;
	}

	public AnimatorFactory getAnimator() {
		return (AnimatorFactory) anicb.getSelectedItem();
	}

	public Board getBoard() {
		return gb.getBoard();
	}

	public GridPattern getPattern() {
		return (GridPattern) patcb.getSelectedItem();
	}

	public boolean isEditAllowed() {
		return modecb.isSelected();
	}

	public boolean isFilePattern() {
		return patrb1.isSelected();
	}

	private JPanel makeCard1() {
		JPanel card = new JPanel(
				new MigLayout("", "fill,grow", "[][grow,fill]"));

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

		gb = new GameBoard(null);
		bl.add(gb, "grow");
		card.add(bl, "spany 2");

		JPanel wh = new JPanel(new MigLayout("", "[right] [grow,fill]"));
		wh.setBorder(BorderFactory.createTitledBorder("Size"));
		JLabel whlw = new JLabel("width");
		wh.add(whlw);
		whtfw = new JFormattedTextField(NumberFormat.getIntegerInstance());
		whtfw.setValue(0);
		whtfw.setColumns(5);
		whtfw.setEditable(false);
		wh.add(whtfw, "wrap");
		JLabel whlh = new JLabel("height");
		wh.add(whlh);
		whtfh = new JFormattedTextField(NumberFormat.getIntegerInstance());
		whtfh.setValue(0);
		whtfh.setColumns(5);
		whtfh.setEditable(false);
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
		brtpf1x = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brtpf1x.setValue(0);
		brtpf1x.setColumns(5);
		brtpf1x.setEditable(false);
		brptf1y = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf1y.setValue(0);
		brptf1y.setColumns(5);
		brptf1y.setEditable(false);
		brp.add(brtpf1x);
		brp.add(brptf1y, "wrap");

		JLabel brplp2 = new JLabel("P2");
		brp.add(brplp2);
		brptf2x = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf2x.setValue(0);
		brptf2x.setColumns(5);
		brptf2x.setEditable(false);
		brptf2y = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf2y.setValue(0);
		brptf2y.setColumns(5);
		brptf2y.setEditable(false);
		brp.add(brptf2x);
		brp.add(brptf2y, "wrap");

		JLabel brplp3 = new JLabel("P3");
		brp.add(brplp3);
		brptf3x = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf3x.setValue(0);
		brptf3x.setColumns(5);
		brptf3x.setEditable(false);
		brptf3y = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf3y.setValue(0);
		brptf3y.setColumns(5);
		brptf3y.setEditable(false);
		brp.add(brptf3x);
		brp.add(brptf3y, "wrap");

		brpb = new JButton("Refresh");
		brp.add(brpb, "span 3");
		brpb.setEnabled(false);

		br.add(brp);
		card.add(br, "grow");

		// ##############################

		toprb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("toprb1");
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
				System.out.println("toprb2");
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
				System.out.println("topb");
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
				System.out.println("blcb");
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
				System.out.println("brrb1");
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
				System.out.println("brrb2");
				form = GridForm.FREE;
				setPointsEnabled(true);

				updateGraph();
				updateButtons();
			}
		});
		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("refersh");
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
		pat.add(patrb1, "wrap,span 2");
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

		modecb = new JCheckBox("Allow editing");
		mode.add(modecb);

		card.add(mode, "grow, wrap");

		JPanel ani = new JPanel(new MigLayout("", "fill, grow"));
		ani.setBorder(BorderFactory.createTitledBorder("Animator"));

		anicb = new JComboBox();
		anicb.addItem(null);
		for (AnimatorFactory aa : AbstractAnimator.getAnimators()) {
			anicb.addItem(aa);
		}
		anicb.setSelectedItem(null);
		ani.add(anicb, "grow");

		card.add(ani, "grow");

		patrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patcb.setEnabled(false);
			}
		});

		patrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patcb.setEnabled(true);
			}
		});

		return card;
	}

	private void setBottomEnabled(boolean b) {
		blcb.setEnabled(b);
		brrb1.setEnabled(b);
		brrb2.setEnabled(b);
		whtfh.setEditable(b);
		whtfw.setEditable(b);
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
			brtpf1x.setValue(points[0].getX());
			brptf1y.setValue(points[0].getY());
			brptf2x.setValue(points[1].getX());
			brptf2y.setValue(points[1].getY());
			brptf3x.setValue(points[2].getX());
			brptf3y.setValue(points[2].getY());
		}
	}

	private void setPointsEnabled(boolean b) {
		brtpf1x.setEditable(b);
		brptf1y.setEditable(b);
		brptf2x.setEditable(b);
		brptf2y.setEditable(b);
		brptf3x.setEditable(b);
		brptf3y.setEditable(b);
		brpb.setEnabled(b);
	}

	private void updateGraph() {
		System.out.println("graph");

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
				brtpf1x.setValue(points[0].getX());
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
						((Number) brtpf1x.getValue()).intValue(),
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
		System.out.println("flip next");
		if (file == null) {
			patrb1.setEnabled(false);
			patrb1.setSelected(false);
			patrb2.setEnabled(false);
			patcb.setEnabled(true);
			patrb2.setSelected(true);
		} else {
			patrb1.setEnabled(true);
			patrb2.setEnabled(true);
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
}
