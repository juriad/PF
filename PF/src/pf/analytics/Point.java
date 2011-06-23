package pf.analytics;

public interface Point {
	int getX();

	int getY();

	boolean isInside(Point p1, Point p2);

	Point move(Vector v);

	Vector positionVector();

	Vector vectorTo(Point p);
}
