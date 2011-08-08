package pf.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpDialog extends CardDialog {
	private static final long serialVersionUID = 1L;
	private static final String title = "help";

	public HelpDialog(JFrame owner) {
		super(owner, title);
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	@Override
	protected boolean canFinish() {
		return true;
	}

	@Override
	protected boolean canNext() {
		return true;
	}

	@Override
	protected boolean canPrev() {
		return true;
	}

	@Override
	protected void flipNext() {
	}

	@Override
	protected void flipPrev() {
	}

	@Override
	protected void makeContent() {
		JPanel card = new JPanel();
		JTextArea ta = new JTextArea(
				"Just create new board, edit it and animate it.", 10, 50);
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		card.add(new JScrollPane(ta));
		addCard(card);
	}

}
