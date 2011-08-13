package pf.gui;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

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
		JPanel card = new JPanel(new BorderLayout());
		JEditorPane html = new JEditorPane();
		html.setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
		html.setEditorKit(kit);
		Document doc = kit.createDefaultDocument();
		html.setDocument(doc);

		card.add(new JScrollPane(html), BorderLayout.CENTER);
		html.setText("<body><p>This application has been developped and coded"
				+ " by Adam Juraszek during most of summer holiday."
				+ "<p>There were approximately 4 previous versions "
				+ "each of them was totaly rewritten and finaly this one "
				+ "got to state which allows user to see at least something."
				+ "<hr>This application is distributed under "
				+ "<a href=\"http://sam.zoy.org/wtfpl/COPYING\">WTFPL</a> licence</body>");
		addCard(card);
	}
}
