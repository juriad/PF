package pf.board;

public enum GridPattern {
	SIMPLE_FULL ("full", true),
	SIMPLE_EMPTY ("empty", true),
	COMPLEX_LIST ("list", false),
	COMPLEX_SCHEMA ("schema", false),
	INTERACTIVE_EDIT ("edit", true),
	INTERACTIVE_SHOW ("show", true);

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

	private GridPattern(String desc, boolean simple) {
		this.desc = desc;
		this.simple = simple;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isSimple() {
		return simple;
	}
}
