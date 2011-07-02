package pf.analytics;

/**
 * Custom version of {@link java.awt.Point}
 * <p>
 * Some methods integrates {@link Vector}.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Point {

	/**
	 * pattern of String representation of point
	 * <p>
	 * implementations shall have a static method
	 * {@code Point fromString(String)}.
	 */
	public static final String pattern = "\\[\\s*-?[0-9]+\\s*,\\s*-?[0-9]+\\s*\\]";

	/**
	 * Gets an x of this point
	 * 
	 * @return x
	 */
	int getX();

	/**
	 * Gets an y of this point
	 * 
	 * @return y
	 */
	int getY();

	/**
	 * Test if this point lies inside of a rectangle defined by two opposite
	 * corners.
	 * <p>
	 * Return value for an edge is implementation specific.
	 * 
	 * @param p1
	 *            corner of rectangle
	 * @param p2
	 *            opposite corner of rectangle
	 * @return true if this point lies inside, false otherwise
	 */
	boolean isInside(Point p1, Point p2);

	/**
	 * Creates a new point: {@code this+vector}
	 * 
	 * @param vector
	 * @return point moved by vector
	 */
	Point move(Vector vector);

	/**
	 * Converts this point into a position vector.
	 * 
	 * @return position vector of this point
	 */
	Vector positionVector();

	/**
	 * Creates vector: {@code point-this}
	 * 
	 * @param point
	 * @return vector from this point to the specified point
	 */
	Vector vectorTo(Point point);
}
