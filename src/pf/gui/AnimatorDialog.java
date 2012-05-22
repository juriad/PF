package pf.gui;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pf.animator.AnimatorFactory;
import pf.animator.Animators;

import net.miginfocom.swing.MigLayout;

/**
 * Dialog for choosing animator, most of its content is used also in
 * {@link NewDialog}
 * <p>
 * Values are acquired from {@link Animators}
 * 
 * @author Adam Juraszek
 * 
 */
public class AnimatorDialog extends CardDialog {
	private static final long serialVersionUID = 1L;
	private final static String title = "Animator dialog";
	private JComboBox acb;

	public AnimatorDialog(JFrame owner) {
		super(owner, title);
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	/**
	 * @return selected animator
	 */
	public AnimatorFactory getAnimator() {
		return (AnimatorFactory) acb.getSelectedItem();
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
		JPanel a = new JPanel(new MigLayout("", "grow,fill"));
		a.setBorder(BorderFactory.createTitledBorder("Pattern"));

		acb = new JComboBox();
		acb.addItem(null);
		for (AnimatorFactory af : Animators.getInstance().getAnimators()) {
			acb.addItem(af);
		}
		acb.setSelectedItem(null);
		a.add(acb);

		card.add(a);
		addCard(card);
	}

}
