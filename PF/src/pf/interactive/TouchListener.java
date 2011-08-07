package pf.interactive;

import java.util.EventListener;

/**
 * This listener's methods are called whenever touch event occurs in
 * {@link InteractiveBoard}
 * 
 * @author Adam Juraszek
 * 
 */
public interface TouchListener extends EventListener {
	/**
	 * Touch has been canceled
	 * 
	 * @param e
	 */
	public void touchCancelled(TouchEvent e);

	/**
	 * Touch has been successfully ended
	 * 
	 * @param e
	 */
	public void touchEnded(TouchEvent e);

	/**
	 * Touch path has become longer
	 * 
	 * @param e
	 */
	public void touchLonger(TouchEvent e);

	/**
	 * Touch path has become shorter
	 * 
	 * @param e
	 */
	public void touchShorter(TouchEvent e);

	/**
	 * Touch has been started
	 * 
	 * @param e
	 */
	public void touchStarted(TouchEvent e);
}
