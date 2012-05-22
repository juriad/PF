package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pf.interactive.EdgesPainterImpl;

import net.miginfocom.swing.MigLayout;

/**
 * Dialog which allows to edit edges painter properties. Each mode has its own
 * properties visualized in a table.
 * 
 * @author Adam Juraszek
 * 
 */
public class EdgesPainterDialog extends CardDialog {

	private static final long serialVersionUID = 1L;

	private JTable editt;

	private JTable showt;

	private JTable runt;

	EdgesPainterImpl editp, showp, runp;

	private JCheckBox editchb;

	private JCheckBox showchb;

	private JCheckBox runchb;

	private JPanel run;

	private JPanel show;

	private JPanel edit;

	private boolean runMode;

	private boolean showMode;

	private boolean editMode;

	public static final String title = "Edges painter dialog";

	public EdgesPainterDialog(JFrame owner, boolean editMode,
			EdgesPainterImpl editp, boolean showMode, EdgesPainterImpl showp,
			boolean runMode, EdgesPainterImpl runp) {
		super(owner, title);
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

	public EdgesPainterImpl getEditEdgesPainter() {
		if (!editchb.isSelected() || !editMode) {
			return null;
		}
		EdgesPainterImpl ep = new EdgesPainterImpl();
		setEp(ep, editt);
		return ep;
	}

	public EdgesPainterImpl getRunEdgesPainter() {
		if (!runchb.isSelected() || !runMode) {
			return null;
		}
		EdgesPainterImpl ep = new EdgesPainterImpl();
		setEp(ep, runt);
		return ep;
	}

	public EdgesPainterImpl getShowEdgesPainter() {
		if (!showchb.isSelected() || !showMode) {
			return null;
		}
		EdgesPainterImpl ep = new EdgesPainterImpl();
		setEp(ep, showt);
		return ep;
	}

	private void fillTable(EdgesPainterImpl vp, JTable t) {
		UniversalTableModel tm = new UniversalTableModel(Color.class,
				BasicStroke.class);
		tm.setColumnNames("State", "Color", "Stroke");
		if (vp == null) {
			vp = new EdgesPainterImpl();
		}
		tm.addRow("Unused", vp.getUnusedColor(), vp.getUnusedStroke());
		tm.addRow("Used", vp.getUsedColor(), vp.getUsedStroke());
		t.setModel(tm);

		t.setDefaultEditor(Color.class, new ColorEditor(this));
		t.setDefaultRenderer(Color.class, new ColorRenderer(true));
		t.setDefaultEditor(BasicStroke.class, new BasicStrokeEditor());
		t.setDefaultRenderer(BasicStroke.class, new BasicStrokeRenderer(true));

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

	private void setEp(EdgesPainterImpl ep, JTable t) {
		UniversalTableModel tm = (UniversalTableModel) t.getModel();
		Object[] unused = tm.getRow("Unused");
		ep.setUnusedColor((Color) unused[0]);
		ep.setUnusedStroke((BasicStroke) unused[1]);
		Object[] used = tm.getRow("Used");
		ep.setUsedColor((Color) used[0]);
		ep.setUsedStroke((BasicStroke) used[1]);
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
