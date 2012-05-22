package pf.board;

import pf.analytics.Point;
import pf.analytics.PointImpl;

/**
 * List of all grid types
 * 
 * @author Adam Juraszek
 * 
 */
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

	/**
	 * @return number of gridlines in this grid type
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * @return points which makes a regular grid
	 */
	public Point[] getRegularPoints() {
		return points;
	}

	public int getUnitSize() {
		return unitSize;
	}

	/**
	 * Tests whether points2 makes a regular grid
	 * 
	 * @param points2
	 * @return true if is regular
	 */
	public boolean isRegular(Point[] points2) {
		if (points2 == null) {
			throw new IllegalArgumentException();
		}
		if (points2.length != points.length) {
			return false;
		}
		for (int i = 0; i < points2.length; i++) {
			if (!points2[i].equals(points[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return getDesc();
	}
}
