package pf.analytics;

import java.util.InputMismatchException;

/**
 * 
 * Default implementation of {@link Point}.
 * <p>
 * Nothing special on this implementation.
 * 
 * @author Adam Juraszek
 * 
 */
public class PointImpl implements Point {
	/**
	 * The origin of coordinate system
	 */
	public static final Point O = new PointImpl(0, 0);

	/**
	 * creates a point from String
	 * <p>
	 * shall work for all Strings which match {@link Point#pattern}
	 * 
	 * @param s
	 * @return Point
	 */
	public static Point fromString(String s) {
		String ss = s.substring(s.indexOf('[') + 1, s.indexOf(']')).trim();
		String[] sss = ss.split(",");
		if (sss.length != 2) {
			throw new InputMismatchException(ss);
		}
		return new PointImpl(Integer.valueOf(sss[0].trim()),
				Integer.valueOf(sss[1].trim()));
	}

	private final int x;

	private final int y;

	/**
	 * The only constructor, requires both x and y.
	 * 
	 * @param x
	 * @param y
	 */
	public PointImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PointImpl)) {
			return false;
		}
		PointImpl other = (PointImpl) obj;
		return x == other.getX() && y == other.getY();
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * true even if this point lies on the edge
	 */
	@Override
	public boolean isInside(Point p1, Point p2) {
		if (p1.getX() <= getX() && p2.getX() >= getX()) {
			if (p1.getY() <= getY() && p2.getY() >= getY()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Point move(Vector v) {
		return new PointImpl(x + v.getX(), y + v.getY());
	}

	@Override
	public Vector positionVector() {
		return new VectorImpl(x, y);
	}

	@Override
	public String toString() {
		return "[ " + x + " , " + y + " ]";
	}

	@Override
	public Vector vectorTo(Point p) {
		return new VectorImpl(p.getX() - x, p.getY() - y);
	}
}
