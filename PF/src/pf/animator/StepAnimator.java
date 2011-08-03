package pf.animator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

public abstract class StepAnimator implements Animator {

	protected class LoopAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public LoopAction() {
			super("Loop animation");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
					KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setLoop((Boolean) getValue(SELECTED_KEY));
		}
	}

	protected enum State {
		RUNNING,
		PAUSED,
		STOPPED
	}

	protected static String playText = "Play";
	protected static String pauseText = "Pause";

	protected static String stopText = "Stop";
	protected int slowStep = 1;
	protected int fastStep = 100;
	protected int defaultStep = 10;

	protected int step = defaultStep;

	protected State state;

	protected JSlider stepSlider;

	protected JPanel control;
	protected JButton playPauseButton;
	protected JButton stopButton;

	private boolean loop;

	public StepAnimator() {
		state = State.STOPPED;
		setLoop(false);
		makeControl();
		updateControl();
	}

	@Override
	public boolean canPause() {
		return isRunning();
	}

	@Override
	public boolean canStart() {
		return isStopped() || isPaused();
	}

	@Override
	public Component getAnimatorControl() {
		return control;
	}

	@Override
	public abstract AnimatorFactory getFactory();

	public boolean isLoop() {
		return loop;
	}

	@Override
	public boolean isPaused() {
		return state.equals(State.PAUSED);
	}

	@Override
	public boolean isRunning() {
		return state.equals(State.RUNNING);
	}

	@Override
	public boolean isStopped() {
		return state.equals(State.STOPPED);
	}

	@Override
	public void pause() {
		state = State.PAUSED;
		updateControl();
	}

	@Override
	public void run() {
		boolean finished = false;
		while (!state.equals(State.STOPPED)) {
			if (state.equals(State.PAUSED)) {
				try {
					wait();
				} catch (InterruptedException ex) {
				}
			} else {
				finished = step();
				if (finished) {
					break;
				}

				try {
					Thread.sleep(1000 / step);
				} catch (InterruptedException ex) {
				}
			}
		}
		if (finished) {
			stop();
			if (isLoop()) {
				start();
			}
		}
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	@Override
	public void setMenu(JMenu menu) {
		menu.add(new JCheckBoxMenuItem(new LoopAction()));
	}

	@Override
	public void start() {
		if (isPaused()) {
			state = State.RUNNING;
			notify();
		} else {
			state = State.RUNNING;
			init();
			new Thread(this).start();
		}
		updateControl();
	}

	@Override
	public void stop() {
		state = State.STOPPED;
		stepSlider.setValue(step);
		updateControl();
	}

	protected abstract void init();

	protected void makeControl() {
		control = new JPanel(new BorderLayout());
		control.add(makeStepControl());
	}

	protected JPanel makeStepControl() {
		JPanel stepControl = new JPanel(new MigLayout("", "[grow,fill] [] []"));
		stepSlider = new JSlider(SwingConstants.HORIZONTAL, slowStep, fastStep,
				defaultStep);
		stepControl.add(stepSlider);
		stepSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				step = stepSlider.getValue();
			}
		});
		playPauseButton = new JButton();
		playPauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (state) {
				case PAUSED:
					start();
					break;
				case RUNNING:
					pause();
					break;
				case STOPPED:
					stop();
					break;
				}
			}
		});
		stepControl.add(playPauseButton);
		stopButton = new JButton();
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		stepControl.add(stopButton);
		return stepControl;
	}

	protected abstract boolean step();

	protected void updateControl() {
		switch (state) {
		case PAUSED:
			playPauseButton.setText(playText);
			playPauseButton.setEnabled(true);
			stopButton.setText(stopText);
			stopButton.setEnabled(true);
			break;
		case RUNNING:
			playPauseButton.setText(pauseText);
			playPauseButton.setEnabled(true);
			stopButton.setText(stopText);
			stopButton.setEnabled(true);
			break;
		case STOPPED:
			playPauseButton.setText(playText);
			playPauseButton.setEnabled(true);
			stopButton.setText(stopText);
			stopButton.setEnabled(false);
			break;
		}
	}

}
