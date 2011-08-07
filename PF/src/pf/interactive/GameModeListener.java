package pf.interactive;

import java.util.EventListener;

/**
 * This listener is notified whenever {@link InteractiveBoard} mode is changed.
 * 
 * @author Adam Juraszek
 * 
 */
public interface GameModeListener extends EventListener {
	/**
	 * Called when new mode is edit
	 * 
	 * @param e
	 */
	public void modeEdit(GameModeEvent e);

	/**
	 * Called when new mode is run
	 * 
	 * @param e
	 */
	public void modeRun(GameModeEvent e);

	/**
	 * Called when new mode is show
	 * 
	 * @param e
	 */
	public void modeShow(GameModeEvent e);
}
