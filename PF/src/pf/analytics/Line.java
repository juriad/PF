package pf.analytics;

public interface Line {
	boolean contains(Point p);

	double distanceSq(Point p);

	Line extend(float f);

	Vector getBaseVector();

	Point getP1();

	Point getP2();

	Point getPoint(float f);

	Point intersection(Line l);

	Line move(Vector v);

	Line moveTo(Point p);
}
