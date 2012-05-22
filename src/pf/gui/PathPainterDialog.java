package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pf.interactive.PathPainterImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Allows to edit path painter properties.
 * 
 * @author Adam Juraszek
 * 
 */
public class PathPainterDialog extends CardDialog {

	private static final long serialVersionUID = 1L;
	public static final String title = "Path painters dialog";
	private JTable t;
	private final List<PathPainterImpl> list;

	public PathPainterDialog(JFrame owner, List<PathPainterImpl> list) {
		super(owner, title);
		this.list = list;
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
		UniversalTableModel tm = (UniversalTableModel) t.getModel();
		for (int i = 0; i < tm.getRowCount() - 1; i++) {
			PathPainterImpl pp = list.get(i);
			Object[] row = tm.getRow("" + (i + 1));
			pp.setColor((Color) row[0]);
			pp.setStroke((BasicStroke) row[1]);
			pp.setCornerRadius((Float) row[2]);
		}
		return list;
	}

	private void setBoard(List<PathPainterImpl> list) {
		UniversalTableModel tm = new UniversalTableModel(Color.class,
				BasicStroke.class, Float.class);
		tm.setColumnNames("Path no.", "Color", "Stroke", "Corner radius");
		t.setModel(tm);
		for (int i = 0; i < list.size(); i++) {
			PathPainterImpl pp = list.get(i);
			tm.addRow("" + (i + 1), pp.getColor(), pp.getStroke(),
					pp.getCornerRadius());
		}

		t.setDefaultEditor(Color.class, new ColorEditor(this));
		t.setDefaultRenderer(Color.class, new ColorRenderer(true));
		t.setDefaultEditor(BasicStroke.class, new BasicStrokeEditor());
		t.setDefaultRenderer(BasicStroke.class, new BasicStrokeRenderer(true));

		t.getColumnModel().getColumn(3)
				.setCellEditor(new FloatRangeEditor(0, 0.5f));
		t.setPreferredScrollableViewportSize(new Dimension((int) (t
				.getPreferredSize().getWidth() * 1.5), (int) t
				.getPreferredSize().getHeight()));
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
		t = new JTable();
		card.add(new JScrollPane(t));

		addCard(card);
	}

}
