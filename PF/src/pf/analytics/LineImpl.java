package pf.analytics;

public class LineImpl implements Line {

	private final static double eps = 1;

	public static int det(int a, int b, int c, int d) {
		return a * d - b * c;
	}

	private final Point p1;

	private final Point p2;

	public LineImpl(Point p1, Point p2) {
		if (p1 == null || p2 == null || p1.equals(p2))
			throw new IllegalArgumentException();
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean contains(Point p) {
		return distanceSq(p) < eps;
	}

	@Override
	public double distanceSq(Point p) {
		int d1 = (getP2().getX() - getP1().getX())
				* (getP1().getY() - p.getY());
		int d2 = (getP1().getX() - p.getX())
				* (getP2().getY() - getP1().getY());
		return (d1 - d2) * (d1 - d2) / ((double) getBaseVector().lengthSq());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineImpl other = (LineImpl) obj;
		if (getBaseVector().isLinearDependent(other.getBaseVector()))
			if (other.contains(p1))
				return true;
		return false;
	}

	@Override
	public Line extend(float f) {
		Vector b = getBaseVector();
		Point pp2 = p2.move(b.scale(f));
		Point pp1 = p1.move(b.scale(-f));
		return new LineImpl(pp1, pp2);
	}

	@Override
	public Vector getBaseVector() {
		return p1.vectorTo(p2);
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

	@Override
	public Point intersection(Line l) {
		int a = getP2().getX() - getP1().getX();
		int b = l.getP1().getX() - l.getP2().getX();
		int c = getP2().getY() - getP1().getY();
		int d = l.getP1().getY() - l.getP2().getY();
		int e = l.getP1().getX() - getP1().getX();
		int f = l.getP1().getY() - getP1().getY();

		int det = det(a, b, c, d);
		if (det == 0)
			return null;
		return this.getPoint(det(e, b, f, d) / det);
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
