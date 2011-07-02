package pf.analytics;

/**
 * Standard vector known from analytic geometry. This is integer based, so not
 * all nuances can be represented.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Vector {
	/**
	 * Calculates addition of this vector and vector
	 * 
	 * @param vector
	 * @return addition
	 */
	Vector addVector(Vector vector);

	/**
	 * Gets x part of this vector
	 * 
	 * @return x
	 */
	int getX();

	/**
	 * Gets y part of this vector
	 * 
	 * @return y
	 */
	int getY();

	/**
	 * Tests if both vectors have the same direction.
	 * 
	 * @param vector
	 *            vector to compare with
	 * @return true if both vectors are oriented the same direction, false
	 *         otherwise
	 */
	boolean isLinearDependent(Vector vector);

	/**
	 * Calculates length squared of this vector.
	 * 
	 * @return length squared
	 */
	int lengthSq();

	/**
	 * Creates a new vector which is perpendicular to this. Both vector may have
	 * the same length -- this is implementation specific.
	 * 
	 * @return normal vector
	 */
	Vector normal();

	/**
	 * Creates an opposite vector: {@code -this}
	 * 
	 * @return opposite vector
	 */
	Vector opposite();

	/**
	 * Creates a new vector which is linear dependent but has a different length
	 * <p>
	 * for factor = 1: returned vector has the same length
	 * 
	 * @param factor
	 *            factor of length extension
	 * @return scaled vector
	 */
	Vector scale(float factor);

	/**
	 * Suppose this vector is a position vector of a point.
	 * 
	 * @return point
	 */
	Point toPoint();
}
