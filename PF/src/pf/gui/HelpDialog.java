package pf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

public class HelpDialog extends CardDialog {
	private static final long serialVersionUID = 1L;
	private static final String title = "help";
	private JEditorPane html;
	private JScrollPane pane;
	private List<String> pages;

	public HelpDialog(JFrame owner) {
		super(owner, title);
		setPreferredSize(new Dimension(400, 300));
		pack();
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	private void createPageTexts() {
		pages = new ArrayList<String>();
		pages.add("<h1>This help</h1>"
				+ "<p>This help will provide you "
				+ "on several pages nearly all information about using this application."
				+ "<p>Press next to start.");
		pages.add("<h1>New board</h1>"
				+ "<p>Use menu Board:New to show a dialog for creating a new board."
				+ " You can load saved board from file, keep it as it is or edit slightly"
				+ " or you can create your own from scratch."
				+ "<p>Set size and choose type of grid. Each type is provides different"
				+ " ways how vertices may be connected."
				+ "<p>You can leave grid regular (it will be square and equilateral)"
				+ " or edit control points to have your own layout."
				+ " A small view of future board is shown on the left."
				+ " <p>Beeing satisfied, go to the next page to set pattern of edges and animator."
				+ " Pattern determines which vertices will be connected by edges."
				+ " Only simple ones are provided for a custom board."
				+ "<p>At last set animator or leave it unset to animate board;"
				+ " there are two of them available.");
		pages.add("<h1>Managing board</h1>"
				+ "<p>Use menu Board:Save to save this board for later use."
				+ " It will ask you to choose pattern type: way how edges are saved to file."
				+ "<p>If you want to add or remove some edges, switch to edit mode."
				+ " Press mouse button and drag along the edge as long as there are edges to edit."
				+ " Then switch back to show mode."
				+ "<p>Application will not ask you when exiting application to save your board.");
		pages.add("<h1>Styling board</h1>"
				+ "<p>Use Painters menu to change the way how graphics is painted."
				+ "<dl>"
				+ "<dt>Edges painter</dt><dd>Defines how edges are painted;"
				+ "each edge can be used or unused -- usage is used in edit and may be also in run mode.</dd>"
				+ "<dt>Grid painter</dt><dd>Defines how grid is painted, each direction is configurable</dd>"
				+ "<dt>Vertices painter</dt><dd>Defines how vertices are painted, "
				+ "degree of vertex is number of edges leading from that vertex; you can choose what edges to calculate.</dd>"
				+ "</dl>");
		pages.add("<h1>Animating board</h1>"
				+ "Choose an animator either in a New-dialog or Animators menu and switch to run mode."
				+ " You may set additional properties in Animators menu before start,"
				+ " some of them cannot be configured while running."
				+ "<p>Control your animation and have fun!");
	}

	private void flip() {
		getCurrentCard().add(pane, BorderLayout.CENTER);
		html.setText(pages.get(getCurrent()));
		html.setCaretPosition(0);
		validate();
		repaint();
	}

	private void makePages(int n) {
		for (int i = 0; i < n; i++) {
			JPanel card = new JPanel(new BorderLayout());
			if (i == 0) {
				pane = new JScrollPane(html);
				card.add(pane, BorderLayout.CENTER);
			}
			addCard(card);
		}
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
		flip();
	}

	@Override
	protected void flipPrev() {
		flip();
	}

	@Override
	protected void makeContent() {
		html = new JEditorPane();
		html.setEditable(false);
		HTMLEditorKit kit = new HTMLEditorKit();
		html.setEditorKit(kit);
		Document doc = kit.createDefaultDocument();
		html.setDocument(doc);

		createPageTexts();
		makePages(pages.size());
		flip();
	}

}
