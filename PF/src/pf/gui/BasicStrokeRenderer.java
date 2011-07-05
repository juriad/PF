package pf.gui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

class BasicStrokeRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;
	Border unselectedBorder = null;
	Border selectedBorder = null;
	boolean isBordered = true;

	public BasicStrokeRenderer(boolean isBordered) {
		this.isBordered = isBordered;
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		BasicStroke stroke = (BasicStroke) value;
		if (isBordered) {
			if (isSelected) {
				if (selectedBorder == null) {
					selectedBorder = BorderFactory.createMatteBorder(2, 5, 2,
							5, table.getSelectionBackground());
				}
				setBorder(selectedBorder);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2,
							5, table.getBackground());
				}
				setBorder(unselectedBorder);
			}
		}

		setText(stroke.getLineWidth() + "@"
				+ Arrays.toString(stroke.getDashArray()));
		setToolTipText("width@[da, sh]");
		return this;
	}
}