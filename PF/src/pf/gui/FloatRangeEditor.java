package pf.gui;

import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Editor of table cell float value which restricts value to a specified range.
 * 
 * @author Adam Juraszek
 * 
 */
public class FloatRangeEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private JFormattedTextField tf;
	private final float min;
	private final float max;
	private float val;

	public FloatRangeEditor(float min, float max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Object getCellEditorValue() {
		float curval = val;
		try {
			float newval = Float.valueOf(tf.getText());
			if (newval >= min && newval <= max) {
				curval = newval;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return curval;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		tf = new JFormattedTextField(
				NumberFormat.getNumberInstance(Locale.ENGLISH));
		tf.setValue(value);
		val = (Float) value;
		return tf;
	}
}
