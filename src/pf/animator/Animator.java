package pf.animator;

import java.awt.Component;

import javax.swing.JMenu;

/**
 * Animator, which animates board in run mode.
 * <p>
 * Each animator should contain construction like this to register itself to
 * {@link Animators}: <br>
 * <code>
 * static {
		Animators.getInstance().addAnimator(AnimatorFactory.getFactory());
	}
 * </code>
 * 
 * @author Adam Juraszek
 * 
 */
public interface Animator extends Runnable {
	/**
	 * @return whether animator can be paused in current state
	 */
	boolean canPause();

	/**
	 * @return whether animator can be started from current state
	 */
	boolean canStart();

	/**
	 * @return control of this animator
	 */
	Component getAnimatorControl();

	/**
	 * @return factory of this animator
	 */
	AnimatorFactory getFactory();

	/**
	 * @return whether animator is currently finished
	 */
	boolean isFinished();

	/**
	 * @return whether animator is currently paused
	 */
	boolean isPaused();

	/**
	 * @return whether animator is currently running
	 */
	boolean isRunning();

	/**
	 * @return whether animator is currently stopped
	 */
	boolean isStopped();

	/**
	 * Pauses running animator.
	 */
	void pause();

	/**
	 * Sets animator menu.
	 * 
	 * @param menu
	 */
	void setMenu(JMenu menu);

	/**
	 * Starts animator thread.
	 */
	void start();

	/**
	 * Stops animator.
	 */
	void stop();
}
