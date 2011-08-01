package pf.interactive;

import java.awt.Component;

public interface Animator extends Runnable {
	public boolean canPause();

	public boolean canStart();

	public boolean isPaused();

	public boolean isRunning();

	public boolean isStopped();

	public void pause();

	public void reset();

	public void setMenu();

	public void start();

	public void stop();

	Component getAnimatorControl();

	AnimatorFactory getFactory();
}
