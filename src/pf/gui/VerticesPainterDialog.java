package pf.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import pf.board.GridType;
import pf.interactive.VerticesPainterImpl;
import pf.interactive.VerticesPainterImpl.DegreeType;

import net.miginfocom.swing.MigLayout;

/**
 * Dialog which allows to edit vertices painter properties. Each mode has its
 * own properties visualized in a table.
 * 
 * @author Adam Juraszek
 * 
 */
public class VerticesPainterDialog extends CardDialog {
	class RadiusEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private int col;
		private int inner;
		private int outer;
		private JFormattedTextField tf;

		@Override
		public Object getCellEditorValue() {
			try {
				int curval = Integer.valueOf(tf.getText());
				tf.setValue(curval);
			} catch (NumberFormatException ex) {
			}
			int val = ((Number) tf.getValue()).intValue();
			if (col == 2) {
				if (val >= inner) {
					return val;
				}
				return outer;
			} else if (col == 3) {
				if (val <= outer && val >= 0) {
					return val;
				}
				return inner;
			} else {
				throw new IllegalStateException();
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			col = column;
			if (col == 2) {
				outer = (Integer) value;
				inner = (Integer) table.getValueAt(row, column + 1);
			} else if (col == 3) {
				inner = (Integer) value;
				outer = (Integer) table.getValueAt(row, column - 1);
			}
			tf = new JFormattedTextField(NumberFormat.getIntegerInstance());
			tf.setValue(value);
			return tf;
		}
	}

	private static final long serialVersionUID = 1L;
	public static final String title = "Vertices painter dialog";

	private final GridType gt;

	private JComboBox editcb;

	private JTable editt;

	private JComboBox showcb;

	private JTable showt;

	private JTable runt;

	private JComboBox runcb;

	VerticesPainterImpl editp, showp, runp;

	private JCheckBox editchb;

	private JCheckBox showchb;

	private JCheckBox runchb;

	private JPanel run;

	private JPanel show;

	private JPanel edit;

	private boolean runMode;

	private boolean showMode;

	private boolean editMode;

	public VerticesPainterDialog(JFrame owner, boolean editMode,
			VerticesPainterImpl editp, boolean showMode,
			VerticesPainterImpl showp, boolean runMode,
			VerticesPainterImpl runp, GridType gt) {
		super(owner, title);
		this.gt = gt;
		this.editMode = editMode;
		this.editp = editp;
		this.showMode = showMode;
		this.showp = showp;
		this.runMode = runMode;
		this.runp = runp;
		setBoard();
		pack();
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	public VerticesPainterImpl getEditVerticesPainter() {
		if (!editchb.isSelected() || !editMode) {
			return null;
		}
		VerticesPainterImpl vp = new VerticesPainterImpl(gt,
				(DegreeType) editcb.getSelectedItem());
		setVp(vp, editt);
		return vp;
	}

	public VerticesPainterImpl getRunVerticesPainter() {
		if (!runchb.isSelected() || !runMode) {
			return null;
		}
		VerticesPainterImpl vp = new VerticesPainterImpl(gt,
				(DegreeType) runcb.getSelectedItem());
		setVp(vp, runt);
		return vp;
	}

	public VerticesPainterImpl getShowVerticesPainter() {
		if (!showchb.isSelected() || !showMode) {
			return null;
		}
		VerticesPainterImpl vp = new VerticesPainterImpl(gt,
				(DegreeType) showcb.getSelectedItem());
		setVp(vp, showt);
		return vp;
	}

	private void fillTable(VerticesPainterImpl vp, JTable t) {
		UniversalTableModel tm = new UniversalTableModel(Color.class,
				Integer.class, Integer.class);
		tm.setColumnNames("Degree", "Color", "Outer", "Inner");
		if (vp == null) {
			vp = new VerticesPainterImpl(gt, DegreeType.BY_UNUSED);
		}
		for (int i = 0; i <= gt.getLines() * 2; i++) {
			tm.addRow("" + i, vp.getColor(i), vp.getOuterRadius(i),
					vp.getInnerRadius(i));
		}
		t.setModel(tm);
		t.setDefaultEditor(Color.class, new ColorEditor(this));
		t.setDefaultRenderer(Color.class, new ColorRenderer(true));
		t.getColumnModel().getColumn(2).setCellEditor(new RadiusEditor());
		t.getColumnModel().getColumn(3).setCellEditor(new RadiusEditor());
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
				for (DegreeType dt : DegreeType.values()) {
					if (dt.equals(editp.getDegreeType())) {
						editcb.setSelectedItem(dt);
					}
				}
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
				for (DegreeType dt : DegreeType.values()) {
					if (dt.equals(showp.getDegreeType())) {
						showcb.setSelectedItem(dt);
					}
				}
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
				for (DegreeType dt : DegreeType.values()) {
					if (dt.equals(runp.getDegreeType())) {
						runcb.setSelectedItem(dt);
					}
				}
				runt.setEnabled(true);
			}
			fillTable(runp, runt);
		}
	}

	private void setVp(VerticesPainterImpl vp, JTable t) {
		UniversalTableModel tm = (UniversalTableModel) t.getModel();
		for (int i = 0; i < tm.getRowCount(); i++) {
			Object[] row = tm.getRow("" + i);
			vp.setColor(i, (Color) row[0]);
			vp.setRadius(i, (Integer) row[1], (Integer) row[2]);
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

		editchb = new JCheckBox("Vertices painter allowed");
		editchb.setSelected(true);
		edit.add(editchb, "span 2,wrap");

		JLabel editl = new JLabel("Degree calculation by (edges)");
		edit.add(editl);

		editcb = new JComboBox();
		for (DegreeType dt : DegreeType.values()) {
			editcb.addItem(dt);
			if (dt.equals(DegreeType.BY_UNUSED)) {
				editcb.setSelectedItem(dt);
			}
		}
		edit.add(editcb, "wrap");

		editt = new JTable();
		edit.add(new JScrollPane(editt), "grow, span 2");

		card.add(edit, "wrap,grow");

		editchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = editchb.isSelected();
				editcb.setEnabled(b);
				editt.setEnabled(b);
			}
		});

		show = new JPanel(new MigLayout("", "fill,grow"));
		show.setBorder(BorderFactory.createTitledBorder("Show mode"));

		showchb = new JCheckBox("Vertices painter allowed");
		showchb.setSelected(true);
		show.add(showchb, "span 2,wrap");

		JLabel showl = new JLabel("Degree calculation by (edges)");
		show.add(showl);

		showcb = new JComboBox();
		for (DegreeType dt : DegreeType.values()) {
			showcb.addItem(dt);
			if (dt.equals(DegreeType.BY_UNUSED)) {
				showcb.setSelectedItem(dt);
			}
		}
		show.add(showcb, "wrap");

		showt = new JTable();
		show.add(new JScrollPane(showt), "grow, span 2");

		card.add(show, "wrap, grow");

		showchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = showchb.isSelected();
				showcb.setEnabled(b);
				showt.setEnabled(b);
			}
		});

		run = new JPanel(new MigLayout("", "fill,grow"));
		run.setBorder(BorderFactory.createTitledBorder("Run mode"));

		runchb = new JCheckBox("Vertices painter allowed");
		runchb.setSelected(true);
		run.add(runchb, "span 2,wrap");

		JLabel runl = new JLabel("Degree calculation by (edges)");
		run.add(runl);

		runcb = new JComboBox();
		for (DegreeType dt : DegreeType.values()) {
			runcb.addItem(dt);
			if (dt.equals(DegreeType.BY_UNUSED)) {
				runcb.setSelectedItem(dt);
			}
		}
		run.add(runcb, "wrap");

		runt = new JTable();
		run.add(new JScrollPane(runt), "grow, span 2");

		card.add(run);

		runchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = runchb.isSelected();
				runcb.setEnabled(b);
				runt.setEnabled(b);
			}
		});
		addCard(card);
	}

}
