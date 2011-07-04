package pf.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class CardDialog extends JDialog {
	public class CancelAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			closedProperly = false;
			dispose();
		}
	}

	public class FinishAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			closedProperly = true;
			dispose();
		}
	}

	public class NextAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isLast()) {
				cl.next(content);
				current++;
				updateButtons();
				flipNext();
			}
		}
	}

	public class PrevAL implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isFirst()) {
				cl.next(content);
				current--;
				updateButtons();
				flipPrev();
			}
		}
	}

	private static final long serialVersionUID = 1L;
	private final JPanel content;

	private final CardLayout cl;

	private final List<JPanel> cards;
	private int current;

	private boolean closedProperly = false;

	private final JButton prev, next, cancel, finish;

	public CardDialog(JFrame owner) {
		super(owner, "New");
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		content = new JPanel(cl = new CardLayout());
		cards = new ArrayList<JPanel>();
		makeContent();
		if (cards.size() == 0) {
			throw new IllegalStateException("No cards added");
		}
		pane.add(content, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		prev = new JButton("< Prev");
		buttons.add(prev);
		prev.addActionListener(new PrevAL());
		next = new JButton("Next >");
		buttons.add(next);
		next.addActionListener(new NextAL());
		cancel = new JButton("Cancel");
		buttons.add(cancel);
		cancel.addActionListener(new CancelAL());
		finish = new JButton("Finish");
		buttons.add(finish);
		finish.addActionListener(new FinishAL());

		pane.add(buttons, BorderLayout.SOUTH);
		if (cards.size() == 1) {
			prev.setVisible(false);
			next.setVisible(false);
		}
		updateButtons();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
	}

	public boolean isClosedProperly() {
		return closedProperly;
	}

	private boolean isFirst() {
		return current == 0;
	}

	private boolean isLast() {
		return current == cards.size() - 1;
	}

	protected void addCard(JPanel panel) {
		cards.add(panel);
		content.add(panel);
	}

	protected abstract boolean canFinish();

	protected abstract boolean canNext();

	protected abstract boolean canPrev();

	protected abstract void flipNext();

	protected abstract void flipPrev();

	protected int getCurrent() {
		return current;
	}

	protected JPanel getCurrentCard() {
		return cards.get(current);
	}

	protected abstract void makeContent();

	protected void updateButtons() {
		finish.setEnabled(isLast() && canFinish());
		next.setEnabled(!isLast() && canNext());
		prev.setEnabled(!isFirst() && canPrev());
		cancel.setEnabled(true);
	}

}
