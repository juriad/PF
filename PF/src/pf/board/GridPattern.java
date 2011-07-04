package pf.board;

public enum GridPattern {
	SIMPLE_FULL ("full", true, false),
	SIMPLE_EMPTY ("empty", true, false),
	COMPLEX_LIST ("list", false, false),
	COMPLEX_SCHEMA ("schema", false, false),
	INTERACTIVE_EDIT ("edit", true, true),
	INTERACTIVE_SHOW ("show", true, true);

	public static GridPattern getPattern(String desc) {
		for (GridPattern gp : values()) {
			if (gp.getDesc().equals(desc)) {
				return gp;
			}
		}
		return null;
	}

	private final String desc;

	private final boolean simple;

	private final boolean internal;

	private GridPattern(String desc, boolean simple, boolean internal) {
		this.desc = desc;
		this.simple = simple;
		this.internal = internal;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isInternal() {
		return internal;
	}

	public boolean isSimple() {
		return simple;
	}
}
