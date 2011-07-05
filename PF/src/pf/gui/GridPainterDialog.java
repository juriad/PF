package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pf.board.GridType;
import pf.interactive.GridPainter;
import pf.interactive.GridPainterImpl;

import net.miginfocom.swing.MigLayout;

public class GridPainterDialog extends CardDialog {
	class GridTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columnNames = { "Gridline no.", "Color", "Stroke",
				"Main color", "Main stroke", "Main repetition", "Main offset" };
		private ArrayList<Object[]> data = new ArrayList<Object[]>();

		public void addRow(int i, Color color, Stroke stroke, Color mainColor,
				Stroke mainStroke, int repetition, int offset) {
			data.add(new Object[] { i, color, stroke, mainColor, mainStroke,
					repetition, offset });

		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			return data.get(row)[col];
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col < 1) {
				return false;
			}
			return true;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			data.get(row)[col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	private static final long serialVersionUID = 1L;

	private JTable editt;

	private JTable showt;

	private JTable runt;

	GridPainterImpl editp;

	GridPainterImpl showp;

	GridPainterImpl runp;

	private JCheckBox editchb;

	private JCheckBox showchb;

	private JCheckBox runchb;

	private JPanel run;

	private JPanel show;

	private JPanel edit;

	private boolean runMode;

	private boolean showMode;

	private boolean editMode;

	private GridType gt;

	public static final String title = "Grid painter dialog";

	public GridPainterDialog(JFrame owner, boolean editMode,
			GridPainterImpl editp, boolean showMode, GridPainterImpl showp,
			boolean runMode, GridPainterImpl runp, GridType gt) {
		super(owner, title);
		this.editMode = editMode;
		this.editp = editp;
		this.showMode = showMode;
		this.showp = showp;
		this.runMode = runMode;
		this.runp = runp;
		this.gt = gt;
		setBoard();
		pack();
	}

	@Override
	public void cancelled() {

	}

	@Override
	public void finished() {
	}

	public GridPainter getGridPainter() {
		if (!editchb.isSelected() || !editMode) {
			return null;
		}
		GridPainterImpl gp = new GridPainterImpl(gt);
		setGp(gp, editt);
		return gp;
	}

	public GridPainter getRunGridPainter() {
		if (!runchb.isSelected() || !runMode) {
			return null;
		}
		GridPainterImpl gp = new GridPainterImpl(gt);
		setGp(gp, runt);
		return gp;
	}

	public GridPainter getShowGridPainter() {
		if (!showchb.isSelected() || !showMode) {
			return null;
		}
		GridPainterImpl gp = new GridPainterImpl(gt);
		setGp(gp, showt);
		return gp;
	}

	private void fillTable(GridPainterImpl gp, JTable t) {
		GridTableModel tm = new GridTableModel();
		if (gp == null) {
			gp = new GridPainterImpl(gt);
		}
		for (int i = 0; i < gt.getLines(); i++) {
			tm.addRow(i + 1, gp.getColor(i), gp.getStroke(i),
					gp.getMainColor(i), gp.getMainStroke(i),
					gp.getRepetition(i), gp.getOffset(i));
		}
		t.setModel(tm);
		t.getColumnModel().getColumn(1)
				.setCellRenderer(new ColorRenderer(true));
		t.getColumnModel().getColumn(1).setCellEditor(new ColorEditor(this));
		t.getColumnModel().getColumn(2)
				.setCellRenderer(new BasicStrokeRenderer(true));
		t.getColumnModel().getColumn(2).setCellEditor(new BasicStrokeEditor());
		t.getColumnModel().getColumn(3)
				.setCellRenderer(new ColorRenderer(true));
		t.getColumnModel().getColumn(3).setCellEditor(new ColorEditor(this));
		t.getColumnModel().getColumn(4)
				.setCellRenderer(new BasicStrokeRenderer(true));
		t.getColumnModel().getColumn(4).setCellEditor(new BasicStrokeEditor());
		t.getColumnModel().getColumn(5)
				.setCellEditor(new IntegerRangeEditor(1, Integer.MAX_VALUE));
		t.getColumnModel()
				.getColumn(6)
				.setCellEditor(
						new IntegerRangeEditor(Integer.MIN_VALUE,
								Integer.MAX_VALUE));
		t.setPreferredScrollableViewportSize(new Dimension((int) (t
				.getPreferredSize().getWidth() * 1.5), (int) t
				.getPreferredSize().getHeight()));
	}

	private void setBoard() {
		if (!editMode) {
			getCurrentCard().remove(edit);
		} else {
			if (editp == null) {
				if (editchb.isSelected()) {
					editchb.doClick();
				}
			} else {
				editt.setEnabled(true);
			}
			fillTable(editp, editt);
		}
		if (!showMode) {
			getCurrentCard().remove(show);
		} else {
			if (showp == null) {
				if (showchb.isSelected()) {
					showchb.doClick();
				}
			} else {
				showt.setEnabled(true);
			}
			fillTable(showp, showt);
		}
		if (!runMode) {
			getCurrentCard().remove(run);
		} else {
			if (runp == null) {
				if (runchb.isSelected()) {
					runchb.doClick();
				}
			} else {
				runt.setEnabled(true);
			}
			fillTable(runp, runt);
		}
	}

	private void setGp(GridPainterImpl gp, JTable t) {
		TableModel tm = t.getModel();
		for (int i = 0; i < t.getRowCount(); i++) {
			gp.setColor(i, (Color) tm.getValueAt(i, 1));
			gp.setStroke(i, (BasicStroke) tm.getValueAt(i, 2));
			gp.setMainColor(i, (Color) tm.getValueAt(i, 3));
			gp.setMainStroke(i, (BasicStroke) tm.getValueAt(i, 4));
			gp.setRepetition(i, (Integer) tm.getValueAt(i, 5));
			gp.setOffset(i, (Integer) tm.getValueAt(i, 6));
		}
	}

	@Override
	protected boolean canFinish() {
		return true;
	}

	@Override
	protected boolean canNext() {
		return false;
	}

	@Override
	protected boolean canPrev() {
		return false;
	}

	@Override
	protected void flipNext() {
	}

	@Override
	protected void flipPrev() {
	}

	@Override
	protected void makeContent() {
		JPanel card = new JPanel(new MigLayout("", "fill,grow"));

		edit = new JPanel(new MigLayout("", "fill,grow"));
		edit.setBorder(BorderFactory.createTitledBorder("Edit mode"));

		editchb = new JCheckBox("Edges painter allowed");
		editchb.setSelected(true);
		edit.add(editchb, "wrap");

		editt = new JTable();
		edit.add(new JScrollPane(editt), "grow, span 2");

		card.add(edit, "wrap,grow");

		editchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = editchb.isSelected();
				editt.setEnabled(b);
			}
		});

		show = new JPanel(new MigLayout("", "fill,grow"));
		show.setBorder(BorderFactory.createTitledBorder("Show mode"));

		showchb = new JCheckBox("Edges painter allowed");
		showchb.setSelected(true);
		show.add(showchb, "wrap");

		showt = new JTable();
		show.add(new JScrollPane(showt), "grow, span 2");

		card.add(show, "wrap, grow");

		showchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = showchb.isSelected();
				showt.setEnabled(b);
			}
		});

		run = new JPanel(new MigLayout("", "fill,grow"));
		run.setBorder(BorderFactory.createTitledBorder("Run mode"));

		runchb = new JCheckBox("Edges painter allowed");
		runchb.setSelected(true);
		run.add(runchb, "wrap");

		runt = new JTable();
		run.add(new JScrollPane(runt), "grow, span 2");

		card.add(run);

		runchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = runchb.isSelected();
				runt.setEnabled(b);
			}
		});
		addCard(card);
	}

}
