package pf.graph;

/**
 * Represents a particular direction of an {@link Edge}.
 * <p>
 * To have totally independent packages {@link pf.graph} and
 * {@link pf.analytics}, this interface doesn't use advantages of Vectors, it
 * has its own implementation.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Direction {

	/**
	 * Gets difference of x coordinate part.
	 * 
	 * @return dx
	 * @see #getDy()
	 */
	int getDx();

	/**
	 * Gets difference of y coordinate part.
	 * 
	 * @return dy
	 * @see #getDx()
	 */
	int getDy();

	/**
	 * Gets the opposite direction to this.
	 * 
	 * @return opposite direction
	 */
	Direction getOpposite();

	/**
	 * Only one of Directions: this, opposite is primary. This is useful in
	 * iterating over edges which shall return each edge only once.
	 * 
	 * @return true if this direction is primary, false otherwise
	 * @see #getOpposite()
	 */
	boolean isPrimary();
}
