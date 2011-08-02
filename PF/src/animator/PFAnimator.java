package animator;

import java.awt.Component;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pf.interactive.InteractiveBoard;

public class PFAnimator implements Animator {

	protected enum State {
		RUNNING,
		PAUSED,
		STOPPED
	}

	protected static final int slowStep = 1;
	protected static final int fastStep = 100;

	protected int step = 10;
	private final InteractiveBoard board;

	protected State state;

	protected JSlider control;

	protected List<PartialPath> paths;

	private static final AnimatorFactory factory = new AnimatorFactory() {
		@Override
		public Animator newInstance(InteractiveBoard board) {
			return new PFAnimator(board);
		}
	};

	public PFAnimator(InteractiveBoard board) {
		this.board = board;
		state = State.STOPPED;
		makeControl();

	}

	@Override
	public boolean canPause() {
		return state.equals(State.RUNNING);
	}

	@Override
	public boolean canStart() {
		return state.equals(State.STOPPED) || state.equals(State.PAUSED);
	}

	@Override
	public Component getAnimatorControl() {
		return control;
	}

	@Override
	public AnimatorFactory getFactory() {
		return factory;
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
	}

	@Override
	public void run() {
		init();
		while (!state.equals(State.STOPPED)) {
			if (state.equals(State.PAUSED)) {
				try {
					wait();
				} catch (InterruptedException ex) {
				}
			} else {
				step();
				try {
					Thread.sleep(1000 / step);
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	@Override
	public void setMenu(JMenu menu) {
		// TODO autogen method: setMenu

	}

	@Override
	public void start() {
		if (isPaused()) {
			state = State.RUNNING;
			notify();
		} else {
			state = State.RUNNING;
			new Thread(this).start();
		}
	}

	@Override
	public void stop() {
		state = State.STOPPED;
	}

	protected void init() {
		board.removePaths();
		board.repaint();
	}

	protected void makeControl() {
		control = new JSlider(SwingConstants.HORIZONTAL, slowStep, fastStep,
				step);
		control.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				step = control.getValue();
			}
		});
	}

	protected void step() {

	}

}
