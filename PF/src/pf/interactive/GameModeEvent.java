package pf.interactive;

import java.util.EventObject;

public class GameModeEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private final GameMode oldValue;
	private final GameMode newValue;

	public GameModeEvent(Object source, GameMode oldValue, GameMode newValue) {
		super(source);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public GameMode getNewValue() {
		return newValue;
	}

	public GameMode getOldValue() {
		return oldValue;
	}
}
