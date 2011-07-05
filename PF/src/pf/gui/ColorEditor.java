package pf.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ColorEditor extends AbstractCellEditor implements TableCellEditor,
		ActionListener {
	private final VerticesPainterDialog verticesPainterDialog;
	private static final long serialVersionUID = 1L;
	protected Color currentColor;
	private JButton button;
	protected static final String EDIT = "edit";

	public ColorEditor(VerticesPainterDialog verticesPainterDialog) {
		this.verticesPainterDialog = verticesPainterDialog;
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			button.setBackground(currentColor);
			Color c = JColorChooser.showDialog(verticesPainterDialog, EDIT,
					currentColor);
			if (c != null) {
				currentColor = c;
			}
			fireEditingStopped();

		}
	}

	@Override
	public Object getCellEditorValue() {
		return currentColor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		currentColor = (Color) value;
		return button;
	}
}