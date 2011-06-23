package pf.analytics;

public interface Line {
	boolean contains(Point p);

	int distanceSq(Point p);

	Vector getBaseVector();

	Point getP1();

	Point getP2();

	Point getPoint(float f);

	Point intersection(Line l);

	Line move(Vector v);

	Line moveTo(Point p);

	Point projection(Point p);
}
