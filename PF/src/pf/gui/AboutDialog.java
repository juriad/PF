package pf.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutDialog extends CardDialog {
	private static final long serialVersionUID = 1;
	private static final String title = "About";

	public AboutDialog(JFrame owner) {
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
		JPanel card = new JPanel();
		JTextArea ta = new JTextArea(
				"This application has been developped and coded"
						+ " by Adam Juraszek during most of summer holiday.\n"
						+ "There were approximately 4 previous versions "
						+ "each of them was totaly rewritten and finaly this one "
						+ "got to state which allows user to see at least something."
						+ "\nThis application is distributed under "
						+ "http://sam.zoy.org/wtfpl/COPYING licence", 10, 50);
		ta.setEditable(false);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		card.add(new JScrollPane(ta));
		addCard(card);
	}
}
