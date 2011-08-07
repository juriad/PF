package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import pf.interactive.PathPainterImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Allows to edit path painter properties.
 * 
 * @author Adam Juraszek
 * 
 */
public class PathPainterDialog extends CardDialog {

	public class PathPainterModel extends AbstractTableModel {

		private static final long serialVersionUID = 7065608702897337372L;

		private String[] columnNames = { "Color", "Stroke", "Corner radius" };

		private ArrayList<PathPainterImpl> data = new ArrayList<PathPainterImpl>();

		public void addRow(PathPainterImpl pp) {
			data.add(pp);
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
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return data.get(rowIndex).getColor();
			case 1:
				return data.get(rowIndex).getStroke();
			case 2:
				return data.get(rowIndex).getCornerRadius();
			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return true;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			switch (col) {
			case 0:
				data.get(row).setColor((Color) value);
				break;
			case 1:
				data.get(row).setStroke((BasicStroke) value);
				break;
			case 2:
				data.get(row).setCornerRadius((Float) value);
				break;
			default:
				break;
			}
			fireTableCellUpdated(row, col);
		}

		protected List<PathPainterImpl> getData() {
			return data;
		}

	}

	private static final long serialVersionUID = 1L;
	public static final String title = "Path painters dialog";
	private JTable ppt;
	private PathPainterModel tm;

	public PathPainterDialog(JFrame owner, List<PathPainterImpl> list) {
		super(owner, title);
		setBoard(list);
		pack();
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	public List<PathPainterImpl> getPathPainters() {
		return tm.getData();
	}

	private void setBoard(List<PathPainterImpl> list) {
		tm = new PathPainterModel();
		ppt.setModel(tm);
		addPathPainters(list);
		ppt.getColumnModel().getColumn(0)
				.setCellRenderer(new ColorRenderer(true));
		ppt.getColumnModel().getColumn(0).setCellEditor(new ColorEditor(this));
		ppt.getColumnModel().getColumn(1)
				.setCellRenderer(new BasicStrokeRenderer(true));
		ppt.getColumnModel().getColumn(1)
				.setCellEditor(new BasicStrokeEditor());
		ppt.getColumnModel().getColumn(2)
				.setCellEditor(new FloatRangeEditor(0, 0.5f));
		ppt.setPreferredScrollableViewportSize(new Dimension((int) (ppt
				.getPreferredSize().getWidth() * 1.5), (int) ppt
				.getPreferredSize().getHeight()));
	}

	protected void addPathPainter(PathPainterImpl pp) {
		tm.addRow(pp);
	}

	protected void addPathPainters(List<PathPainterImpl> list) {
		for (PathPainterImpl pp : list) {
			addPathPainter(pp);
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
		JPanel card = new JPanel(new MigLayout("", "grow,fill"));
		ppt = new JTable();
		card.add(new JScrollPane(ppt));

		addCard(card);
	}

}
