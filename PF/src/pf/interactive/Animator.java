package pf.interactive;

import java.awt.Component;

public interface Animator extends Runnable {
	public long elapsedTime();

	public boolean isPaused();

	public boolean isRunning();

	public boolean isStopped();

	public void pause();

	public long remainingTime();

	public void reset();

	public void stop();

	Component getAnimatorControl();

	AnimatorFactory getFactory();
}
