package pf.interactive;

import pf.analytics.Point;
import pf.graph.Vertex;

/**
 * This class responds to snapping requests of {@link InteractiveBoard}
 * 
 * @author Adam Juraszek
 * 
 */
public interface SnapPolicy {
	/**
	 * Returns the vertex to which cursor should be snapped on a board
	 * 
	 * @param board
	 * @param x
	 * @param y
	 * @param last
	 * @return vertex
	 */
	Vertex request(GameBoard board, float x, float y, Point last);
}
