package pf.gui;

public interface Animator extends Runnable {
	public long elapsedTime();

	public boolean isPaused();

	public boolean isRunning();

	public boolean isStopped();

	public void pause();

	public long remainingTime();

	public void stop();
}
