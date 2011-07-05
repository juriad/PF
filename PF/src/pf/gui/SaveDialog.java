package pf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pf.board.Board;
import pf.board.GridPattern;

import net.miginfocom.swing.MigLayout;

public class SaveDialog extends CardDialog {

	public static final String title = "Save dialog";

	private final Board board;

	private File file = null;

	private JComboBox ppcb;

	private static final long serialVersionUID = 1L;

	public SaveDialog(JDialog owner, Board board) {
		super(owner, title);
		this.board = board;
	}

	public SaveDialog(JFrame owner, Board board) {
		super(owner, title);
		this.board = board;
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
		try {
			board.save(file, (GridPattern) ppcb.getSelectedItem());
			JOptionPane.showMessageDialog(getOwner(), "Board has been saved",
					"Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(getOwner(),
					"A problem occured during saving board to file", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	protected boolean canFinish() {
		return file != null;
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

		JPanel fp = new JPanel(new MigLayout("", "[grow,fill] []"));
		fp.setBorder(BorderFactory.createTitledBorder("File"));

		final JTextField fptf = new JTextField(20);
		fptf.setEditable(false);
		fp.add(fptf, "grow");

		JButton fpb = new JButton("Browse");
		fp.add(fpb);
		fpb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(file);
				int status = fc.showSaveDialog(SaveDialog.this);
				if (status == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					file = f;
					fptf.setText(file.getPath());
					updateButtons();
				}
			}
		});

		card.add(fp, "wrap");

		JPanel pp = new JPanel(new MigLayout("", "grow,fill"));
		pp.setBorder(BorderFactory.createTitledBorder("Pattern"));

		ppcb = new JComboBox();
		for (GridPattern gp : GridPattern.values()) {
			if (!gp.isInternal()) {
				ppcb.addItem(gp);
			}
		}
		ppcb.setSelectedIndex(0);
		pp.add(ppcb);

		card.add(pp);

		addCard(card);

	}

}
