package pf.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import pf.interactive.GameMode;
import pf.interactive.GameModeEvent;
import pf.interactive.GameModeListener;
import pf.interactive.InteractiveBoard;

/**
 * Control panel for {@link InteractiveBoard}. Allows to switch modes.
 * 
 * @author Adam Juraszek
 * 
 */
public class InteractiveBoardControl extends JPanel {
	public class GML implements GameModeListener {

		@Override
		public void modeEdit(GameModeEvent e) {
			setButtons();
		}

		@Override
		public void modeRun(GameModeEvent e) {
			setButtons();
		}

		@Override
		public void modeShow(GameModeEvent e) {
			setButtons();
		}
	}

	private static final long serialVersionUID = 1L;
	private final InteractiveBoard board;
	private final Map<GameMode, JButton> buttons;
	private final Map<JButton, GameMode> modes;

	public InteractiveBoardControl(InteractiveBoard board) {
		this.board = board;
		board.addGameModeListener(new GML());
		setLayout(new FlowLayout());

		buttons = new HashMap<GameMode, JButton>();
		modes = new HashMap<JButton, GameMode>();
		for (GameMode gm : GameMode.values()) {
			JButton button = new JButton(gm.getDesc());
			buttons.put(gm, button);
			modes.put(button, gm);
			this.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getBoard().setMode(modes.get(e.getSource()));
				}
			});
		}
		setButtons();
	}

	public JButton get(GameMode mode) {
		return buttons.get(mode);
	}

	public InteractiveBoard getBoard() {
		return board;
	}

	public void setButtons() {
		for (GameMode m : buttons.keySet()) {
			buttons.get(m).setEnabled(getBoard().can(m));
		}
	}
}
