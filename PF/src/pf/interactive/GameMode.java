package pf.interactive;

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
}
