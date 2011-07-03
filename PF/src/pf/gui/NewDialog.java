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
import pf.board.GridType;
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

	private File file = null;
	private BoardImpl.GridForm form;
	private int type;

	private GameBoard gb;

	private JFormattedTextField brptf3y;

	private JFormattedTextField brptf3x;

	private JFormattedTextField brptf2y;

	private JFormattedTextField brptf2x;

	private JFormattedTextField brptf1y;

	private JFormattedTextField brptf1x;

	private JRadioButton brrb2;

	private JRadioButton brrb1;

	private JComboBox blcb;

	private Map<GridType, Integer> types;

	private JFormattedTextField whptfw;

	private JFormattedTextField whptfh;

	public NewDialog(JFrame owner) {
		super(owner);
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

		JPanel whp = new JPanel(new MigLayout("", "[right] [grow,fill]"));
		whp.setBorder(BorderFactory.createTitledBorder("Size"));
		JLabel whplw = new JLabel("width");
		whp.add(whplw);
		whptfw = new JFormattedTextField(NumberFormat.getIntegerInstance());
		whptfw.setValue(0);
		whptfw.setColumns(5);
		whptfw.setEditable(false);
		whp.add(whptfw, "wrap");
		JLabel whplh = new JLabel("height");
		whp.add(whplh);
		whptfh = new JFormattedTextField(NumberFormat.getIntegerInstance());
		whptfh.setValue(0);
		whptfh.setColumns(5);
		whptfh.setEditable(false);
		whp.add(whptfh, "wrap");
		final JButton whpb = new JButton("Refresh");
		whp.add(whpb, "span 2");
		whpb.setEnabled(false);

		card.add(whp, "wrap");

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
		brptf1x = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf1x.setValue(0);
		brptf1x.setColumns(5);
		brptf1x.setEditable(false);
		brptf1y = new JFormattedTextField(NumberFormat.getIntegerInstance());
		brptf1y.setValue(0);
		brptf1y.setColumns(5);
		brptf1y.setEditable(false);
		brp.add(brptf1x);
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

		final JButton brpb = new JButton("Refresh");
		brp.add(brpb, "span 3");
		brpb.setEnabled(false);

		br.add(brp);
		card.add(br, "grow");

		// ##############################

		blcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				type = blcb.getSelectedIndex();
				updateGraph();
				updateButtons();
			}
		});
		toprb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gb.setBoard(null);
				blcb.setEnabled(false);
				brrb1.setEnabled(false);
				brrb2.setEnabled(false);
				brptf1x.setEditable(false);
				brptf1y.setEditable(false);
				brptf2x.setEditable(false);
				brptf2y.setEditable(false);
				brptf3x.setEditable(false);
				brptf3y.setEditable(false);
				brpb.setEnabled(false);
				topb.setEnabled(true);
				whptfh.setEditable(false);
				whptfw.setEditable(false);
				whpb.setEnabled(false);
				updateGraph();
				updateButtons();
			}
		});
		toprb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				file = null;
				blcb.setEnabled(true);
				brrb1.setEnabled(true);
				brrb2.setEnabled(true);
				if (form.equals(GridForm.FREE)) {
					brptf1x.setEditable(true);
					brptf1y.setEditable(true);
					brptf2x.setEditable(true);
					brptf2y.setEditable(true);
					brptf3x.setEditable(true);
					brptf3y.setEditable(true);
					brpb.setEnabled(true);
				}
				whptfh.setEditable(true);
				whptfw.setEditable(true);
				whpb.setEnabled(true);
				topb.setEnabled(false);
				updateGraph();
				updateButtons();
			}
		});
		topb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
			}
		});
		brrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brptf1x.setEditable(false);
				brptf1y.setEditable(false);
				brptf2x.setEditable(false);
				brptf2y.setEditable(false);
				brptf3x.setEditable(false);
				brptf3y.setEditable(false);
				brpb.setEnabled(false);

				Point[] points = ((GridType) blcb.getSelectedItem())
						.getRegularPoints();
				brptf1x.setValue(points[0].getX());
				brptf1y.setValue(points[0].getY());
				brptf2x.setValue(points[1].getX());
				brptf2y.setValue(points[1].getY());
				brptf3x.setValue(points[2].getX());
				brptf3y.setValue(points[2].getY());

				updateGraph();
				updateButtons();
			}
		});
		brrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brptf1x.setEditable(true);
				brptf1y.setEditable(true);
				brptf2x.setEditable(true);
				brptf2y.setEditable(true);
				brptf3x.setEditable(true);
				brptf3y.setEditable(true);
				brpb.setEnabled(true);
				updateGraph();
				updateButtons();
			}
		});
		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateGraph();
				updateButtons();
			}
		};

		whpb.addActionListener(update);
		brpb.addActionListener(update);
		return card;
	}

	private JPanel makeCard2() {
		JPanel card = new JPanel();
		return card;
	}

	private void updateGraph() {
		Board b = null;
		try {
			if (file != null) {

				b = BoardImpl.createBoard(file);
				gb.setBoard(b);

			} else {
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
				b = new BoardImpl(g, ((Number) whptfw.getValue()).intValue(),
						((Number) whptfh.getValue()).intValue());
				gb.setBoard(b);
			}
		} catch (Exception ex) {
			file = null;
			gb.setBoard(null);
			gb.setGridPainter(null);
			gb.setVerticesPainter(null);
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
			whptfw.setValue(b.getWidth());
			whptfh.setValue(b.getHeight());

			gb.setGridPainterAndPaint(new GridPainterImpl((GridType) blcb
					.getSelectedItem()));
			gb.setVerticesPainterAndPaint(new VerticesPainterImpl(
					(GridType) blcb.getSelectedItem(), DegreeType.BY_ALL));
		}
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
	protected void makeContent() {
		types = new HashMap<GridType, Integer>();
		type = 0;
		form = GridForm.REGULAR;
		addCard(makeCard1());
		addCard(makeCard2());
	}
}
