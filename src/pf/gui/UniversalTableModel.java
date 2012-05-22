package pf.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for all dialogs. First column is String and others are
 * configurable. First line is common, setting its value sets all values in
 * column.
 * 
 * @author Adam Juraszek
 * 
 */
public class UniversalTableModel extends AbstractTableModel {

	private Class<?>[] classes;

	private static final long serialVersionUID = 1L;

	protected List<Object[]> rows;
	protected List<String> rowNames;
	protected final String[] columnNames;

	public UniversalTableModel(Class<?>... classes) {
		this.classes = new Class<?>[classes.length + 1];
		System.arraycopy(classes, 0, this.classes, 1, classes.length);
		this.classes[0] = String.class;
		columnNames = new String[getColumnCount()];
		for (int i = 0; i < getColumnCount(); i++) {
			columnNames[i] = i == 0 ? "Name" : classes[i - 1].getSimpleName();
		}
		rows = new ArrayList<Object[]>();
		rowNames = new ArrayList<String>();
	}

	public void addRow(String name, Object... row) {
		if (rows.isEmpty()) {
			rows.add(new Object[getColumnCount() - 1]);
			rowNames.add("Common");
		}
		rows.add(row);
		rowNames.add(name);
		for (int i = 1; i < getColumnCount(); i++) {
			updateSummary(i);
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return classes[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return classes.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object[] getRow(String name) {
		int index = rowNames.indexOf(name);
		return index == -1 ? null : rows.get(index);
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return columnIndex == 0 ? rowNames.get(rowIndex)
				: rows.get(rowIndex)[columnIndex - 1];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col < 1) {
			return false;
		}
		return true;
	}

	public void setColumnNames(String... names) {
		for (int i = 0; i < getColumnCount() && i < names.length; i++) {
			columnNames[i] = names[i];
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col == 0) {
			rowNames.set(row, (String) value);
		} else {
			rows.get(row)[col - 1] = value;
			if (row == 0 && value != null) {
				for (int r = 1; r < getRowCount(); r++) {
					rows.get(r)[col - 1] = value;
					fireTableCellUpdated(r, col);
				}
			} else {
				updateSummary(col);
			}
		}
		fireTableCellUpdated(row, col);
	}

	public void updateSummary(int column) {
		if (column == 0) {
			return;
		}
		if (getRowCount() == 0) {
			return;
		}
		Object common = getValueAt(1, column);
		boolean comm = true;
		for (int r = 2; r < getRowCount(); r++) {
			Object val = getValueAt(r, column);
			if (common == null) {
				if (val == null) {
					// ok
				} else {
					comm = false;
					break;
				}
			} else {
				if (common.equals(val)) {
					// ok
				} else {
					comm = false;
					break;
				}
			}
		}
		rows.get(0)[column - 1] = comm ? common : null;
		fireTableCellUpdated(0, column);
	}

}
