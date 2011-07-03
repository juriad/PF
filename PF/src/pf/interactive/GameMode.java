package pf.interactive;

public enum GameMode {
	EDIT ("Edit"),
	SHOW ("Show"),
	RUN ("Run"),
	PAUSE ("Pause");

	private final String desc;

	private GameMode(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
