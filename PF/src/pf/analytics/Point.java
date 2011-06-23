package pf.analytics;

public interface Point {
	public static final String pattern = "\\[\\s*-?[0-9]+\\s*,\\s*-?[0-9]+\\s*\\]";

	int getX();

	int getY();

	boolean isInside(Point p1, Point p2);

	Point move(Vector v);

	Vector positionVector();

	Vector vectorTo(Point p);
}
