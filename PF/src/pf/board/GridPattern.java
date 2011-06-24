package pf.board;

public enum GridPattern {
	SIMPLE_FULL ("full", true),
	SIMPLE_EMPTY ("empty", true),
	SIMPLE_HEXAGONAL ("hexagonal", true),
	SIMPLE_SQUARE ("square", true),
	SIMPLE_PARALLEL ("parallel", true),
	COMPLEX_LIST ("list", false),
	COMPLEX_SCHEMA ("schema", false);

	public static GridPattern getPattern(String desc) {
		for (GridPattern gp : values())
			if (gp.getDesc().equals(desc))
				return gp;
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
