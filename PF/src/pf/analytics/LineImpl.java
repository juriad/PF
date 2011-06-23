package pf.analytics;

public class LineImpl implements Line {

	@Override
	public String toString() {
		return "LineImpl [p1=" + p1 + ", p2=" + p2 + "]";
	}

	private final static double eps = 1;

	public static int det(int a, int b, int c, int d) {
		return a * d - b * c;
	}

	private final Point p1;

	private final Point p2;

	public LineImpl(Point p1, Point p2) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean contains(Point p) {
		return distanceSq(p) < eps;
	}

	@Override
	public int distanceSq(Point p) {
		Point pp = projection(p);
		return pp.vectorTo(p).lengthSq();
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
	public Point intersection(Line l) {
		int a = getP2().getX() - getP1().getX();
		int b = l.getP1().getX() - l.getP2().getX();
		int c = getP2().getY() - getP1().getY();
		int d = l.getP1().getY() - l.getP2().getY();
		int e = getP1().getX() - l.getP1().getX();
		int f = getP1().getY() - l.getP1().getY();
		System.out.println(a + " " + b + " " + c + " " + d + " " + e + " " + f);

		int det = det(a, b, c, d);
		if (det == 0) {
			return null;
		} else {
			return this.getPoint(det(e, f, c, d) / det);
		}
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
	public Point projection(Point p) {
		Point x = p.move(p1.positionVector().opposite());
		Point a = p2.move(p1.positionVector().opposite());

		int c = a.positionVector().lengthSq();
		int d = a.getX() * x.getX() + a.getY() * x.getY();

		return new PointImpl((a.getX() * d) / c, (a.getY() * d) / c).move(p1
				.positionVector());
	}
}
