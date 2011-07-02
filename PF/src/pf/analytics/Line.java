package pf.analytics;

/**
 * 
 * This represents a line defined by two {@link Point}s.
 * <p>
 * Most methods suppose this line is infinitely long, but this can be
 * implementation specific. Some methods are similar to those in
 * {@link java.awt.geom.Line2D}
 * <p>
 * Because of integer based interface {@link Point} some methods can have an
 * interesting behavior: Point returned by getPoint may not lie (
 * {@code contains==false}) on this line.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Line {

	/**
	 * Base vector is a vector between both points defining this line.
	 * 
	 * @see #getP1()
	 * @see #getP2()
	 * @return vector {@code (P2-P1)}
	 */
	Vector baseVector();

	/**
	 * Tests if this line contains point. This may return true even if the point
	 * lies very near, this is implementation specific behavior. This may also
	 * suppose the line is infinite.
	 * 
	 * @param point
	 * @return true if point lies on the line, false otherwise
	 */
	boolean contains(Point point);

	/**
	 * Calculates distance of point to this line. This should suppose this line
	 * is infinite.
	 * 
	 * @param point
	 * @return distance squared from point to this line
	 */
	double distanceSq(Point point);

	/**
	 * Makes the line factor-times longer to both ends.
	 * <p>
	 * factor=0: returned line is the same <br>
	 * factor=1: returned line is three times longer<br>
	 * factor: returned line is {@code (factor*2+1)} times longer
	 * 
	 * @param factor
	 *            how much bigger is the new line
	 * @return longer line
	 */
	Line extend(float factor);

	/**
	 * One of the points which defines this line
	 * 
	 * @see #getP2()
	 * @return point P1
	 */
	Point getP1();

	/**
	 * The other one of the points which defines this line
	 * 
	 * @see #getP1()
	 * @return point P2
	 */
	Point getP2();

	/**
	 * Line is set of points and each of them is represented by a particular
	 * value of factor.
	 * <p>
	 * For factor=0 returns P1 <br>
	 * For factor=1 returns P2
	 * 
	 * @param factor
	 *            how far from P1 in multiples of base vector
	 * @return point which lies on this line
	 * @see #baseVector()
	 */
	Point getPoint(float factor);

	/**
	 * Calculates intersection of two lines. This should suppose both lines are
	 * infinite.
	 * 
	 * @param line
	 *            line which is tested for intersections with this line
	 * @return intersection point of both lines or null if doesn't exist
	 */
	Point intersection(Line line);

	/**
	 * Creates a new line parallel to this moved by vector.
	 * 
	 * @param vector
	 *            how to move this line
	 * @return parallel line
	 */
	Line move(Vector vector);

	/**
	 * Creates a new parallel line going through point.
	 * 
	 * @param point
	 *            point through a new line goes
	 * @return parallel line
	 */
	Line moveTo(Point point);
}
