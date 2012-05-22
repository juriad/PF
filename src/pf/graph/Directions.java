package pf.graph;

import java.util.Set;

/**
 * All directions are grouped in this class.
 * <p>
 * This is used for iteration over all directions.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Directions extends Iterable<Direction> {

	/**
	 * Gets all directions
	 * 
	 * @return set of directions
	 */
	Set<Direction> getDirections();

	/**
	 * Calculates the nearest direction.
	 * <p>
	 * Let's suppose you have two points and you want to know which direction
	 * has the connector. And to which direction this can be approximated.
	 * 
	 * @param dx
	 * @param dy
	 * @return nearest direction
	 */
	Direction getNearestDirection(int dx, int dy);

	/**
	 * Makes {@link #getNearestDirection(int, int)} interface comfortable for
	 * vertices.
	 * 
	 * @param v1
	 * @param v2
	 * @return nearest direction
	 * @see #getNearestDirection(int, int)
	 */
	Direction getNearestDirection(Vertex v1, Vertex v2);
}