package pf.board;

import pf.analytics.Line;
import pf.analytics.Point;

/**
 * Represents a set of parallel lines.
 * 
 * @author Adam Juraszek
 * 
 */
public interface GridLine extends Line {
	/**
	 * @return distance of two parallel liness
	 */
	double distanceSq();

	/**
	 * @param parallel
	 * @return parallel line with specified index
	 */
	Line getLine(int parallel);

	/**
	 * @param p
	 * @return index of parallel line which is the nearest to the point
	 */
	int getNearest(Point p);

	/**
	 * @param p
	 * @return parallel line which is the nearest to the point
	 * @see #getNearest(Point)
	 */
	Line getNearestLine(Point p);

}
