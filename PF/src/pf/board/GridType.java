package pf.board;

import pf.analytics.Point;
import pf.analytics.PointImpl;

public enum GridType {
	DIAGONAL (4, "diagonal", new Point[] { new PointImpl(0, 0),
			new PointImpl(0, 1), new PointImpl(1, 0) }, 1),
	DIAGONALX (4, "diagonalx", new Point[] { new PointImpl(1, 1),
			new PointImpl(0, 2), new PointImpl(0, 0) }, 2),
	SQUARE (2, "square", new Point[] { new PointImpl(0, 0),
			new PointImpl(0, 1), new PointImpl(1, 0) }, 1),
	TRIANGLE (3, "triangle", new Point[] { new PointImpl(0, 0),
			new PointImpl(4, 7), new PointImpl(8, 0) }, 8);

	public static GridType getType(String desc) {
		for (GridType gt : values()) {
			if (gt.getDesc().equals(desc)) {
				return gt;
			}
		}
		return null;
	}

	private final String desc;
	private final int lines;
	private final int unitSize;

	private final Point[] points;

	private GridType(int lines, String desc, Point[] points, int unitSize) {
		this.lines = lines;
		this.desc = desc;
		this.points = points;
		this.unitSize = unitSize;
	}

	public String getDesc() {
		return desc;
	}

	public int getLines() {
		return lines;
	}

	public Point[] getRegularPoints() {
		return points;
	}

	public int getUnitSize() {
		return unitSize;
	}
}
