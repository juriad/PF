package animator;

import java.awt.Component;

import javax.swing.JMenu;

public interface Animator extends Runnable {
	public boolean canPause();

	public boolean canStart();

	public boolean isPaused();

	public boolean isRunning();

	public boolean isStopped();

	public void pause();

	public void setMenu(JMenu menu);

	public void start();

	public void stop();

	Component getAnimatorControl();

	AnimatorFactory getFactory();
}
