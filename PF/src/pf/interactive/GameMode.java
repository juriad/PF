package pf.interactive;

/**
 * Mode of {@link InteractiveBoard}
 * 
 * @author Adam Juraszek
 * 
 */
public enum GameMode {
	EDIT ("Edit"),
	SHOW ("Show"),
	RUN ("Run");

	private final String desc;

	private GameMode(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return getDesc();
	}
}
