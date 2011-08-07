package pf.gui;

import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Editor of table cell integer value which restricts value to a specified
 * range.
 * 
 * @author Adam Juraszek
 * 
 */
public class IntegerRangeEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private JFormattedTextField tf;
	private final int min;
	private final int max;
	private int val;

	public IntegerRangeEditor(int min, int max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Object getCellEditorValue() {
		int curval = val;
		try {
			int newval = Integer.valueOf(tf.getText());
			if (newval >= min && newval <= max) {
				curval = newval;
			}
		} catch (NumberFormatException ex) {
		}
		return curval;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		tf = new JFormattedTextField(NumberFormat.getIntegerInstance());
		tf.setValue(value);
		val = (Integer) value;
		return tf;
	}
}
