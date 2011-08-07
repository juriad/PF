package pf.analytics;

/**
 * 
 * Default implementation of {@link Line}.
 * <p>
 * This suppose line is infinite and two of them are same iff one covers the
 * other.
 * 
 * @author Adam Juraszek
 * 
 */
public class LineImpl implements Line {

	/**
	 * distance of point to line to be considered as lying on
	 */
	protected static double eps = 0.5;

	/**
	 * Calculates determinant of matrix
	 * <table>
	 * <tr>
	 * <td>a</td>
	 * <td>b</td>
	 * </tr>
	 * <tr>
	 * <td>c</td>
	 * <td>d</td>
	 * </tr>
	 * </table>
	 * <p>
	 * this is used by {@link #intersection(Line)}
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return determinant
	 */
	public static int det(int a, int b, int c, int d) {
		return a * d - b * c;
	}

	private final Point p1;

	private final Point p2;

	/**
	 * The only constructor requires both points which defines the line.
	 * 
	 * @param p1
	 * @param p2
	 * @see #getP1()
	 * @see #getP2()
	 */
	public LineImpl(Point p1, Point p2) {
		if (p1 == null || p2 == null || p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public Vector baseVector() {
		return p1.vectorTo(p2);
	}

	/**
	 * Tests if point is very near: ({@code distanceSq(point) < eps}
	 * 
	 * @see eps
	 */
	@Override
	public boolean contains(Point point) {
		return distanceSq(point) < eps;
	}

	/**
	 * Uses a simple formula from analytic geometry
	 */
	@Override
	public double distanceSq(Point p) {
		int d1 = (getP2().getX() - getP1().getX())
				* (getP1().getY() - p.getY());
		int d2 = (getP1().getX() - p.getX())
				* (getP2().getY() - getP1().getY());
		return (d1 - d2) * (d1 - d2) / ((double) baseVector().lengthSq());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LineImpl other = (LineImpl) obj;
		if (baseVector().isLinearDependent(other.baseVector())) {
			if (other.contains(p1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Line extend(float f) {
		Vector b = baseVector();
		Point pp2 = p2.move(b.scale(f));
		Point pp1 = p1.move(b.scale(-f));
		return new LineImpl(pp1, pp2);
	}

	@Override
	public Point getP1() {
		return p1;
	}

	@Override
	public Point getP2() {
		return p2;
	}

	@Override
	public Point getPoint(float f) {
		return p1.move(p1.vectorTo(p2).scale(f));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) distanceSq(PointImpl.O);
		return result;
	}

	/**
	 * may not return actual intersection because of integer based interface
	 * {@link Point}
	 * <p>
	 * intersection is calculated by Cramer's rule
	 */
	@Override
	public Point intersection(Line l) {
		int a = getP2().getX() - getP1().getX();
		int b = l.getP1().getX() - l.getP2().getX();
		int c = getP2().getY() - getP1().getY();
		int d = l.getP1().getY() - l.getP2().getY();
		int e = l.getP1().getX() - getP1().getX();
		int f = l.getP1().getY() - getP1().getY();

		int det = det(a, b, c, d);
		if (det == 0) {
			return null;
		}
		return getPoint(det(e, b, f, d) / det);
	}

	@Override
	public Line move(Vector v) {
		return new LineImpl(p1.move(v), p2.move(v));
	}

	@Override
	public Line moveTo(Point p) {
		return move(p1.vectorTo(p));
	}

	@Override
	public String toString() {
		return "LineImpl [p1=" + p1 + ", p2=" + p2 + "]";
	}
}
