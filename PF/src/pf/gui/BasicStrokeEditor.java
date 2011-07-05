package pf.gui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class BasicStrokeEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;
	private BasicStroke stroke;
	private JTextField tf;

	@Override
	public Object getCellEditorValue() {
		try {
			Scanner s = new Scanner(tf.getText());
			s.useLocale(Locale.US);
			s.useDelimiter("[@ ,\\[\\]]+");
			float w = s.nextFloat();
			if (s.hasNextFloat()) {
				ArrayList<Float> ds = new ArrayList<Float>();
				while (s.hasNextFloat()) {
					ds.add(s.nextFloat());
				}
				float[] dsf = new float[ds.size()];
				for (int i = 0; i < ds.size(); i++) {
					dsf[i] = ds.get(i);
				}
				stroke = new BasicStroke(w, BasicStroke.CAP_SQUARE,
						BasicStroke.JOIN_MITER, 10, dsf, 10);
			} else {
				stroke = new BasicStroke(w);
			}

		} catch (InputMismatchException ex) {
			ex.printStackTrace();
		}
		return stroke;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		stroke = (BasicStroke) value;
		tf = new JTextField(stroke.getLineWidth() + "@"
				+ Arrays.toString(stroke.getDashArray()));
		return tf;
	}
}