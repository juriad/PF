package pf.interactive;

import java.util.EventObject;

/**
 * Event notifying about game mode change in {@link InteractiveBoard}.
 * 
 * @author Adam Juraszek
 * 
 */
public class GameModeEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private final GameMode oldValue;
	private final GameMode newValue;

	public GameModeEvent(Object source, GameMode oldValue, GameMode newValue) {
		super(source);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return current value of mode in {@link InteractiveBoard}
	 */
	public GameMode getNewValue() {
		return newValue;
	}

	/**
	 * @return previous value of mode in {@link InteractiveBoard}
	 */
	public GameMode getOldValue() {
		return oldValue;
	}
}
