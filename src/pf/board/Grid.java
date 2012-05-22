package pf.board;

import pf.analytics.Point;
import pf.graph.Directions;

/**
 * A grid on a board is a set of gridlines.
 * <p>
 * lowerLimit and upperLimit methods are used for determining range of gridlines
 * in a board.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Grid {
	/**
	 * @param width
	 * @param height
	 * @return new board with specific size and this grid
	 */
	BoardGraph createGraph(int width, int height);

	/**
	 * @return directions of all gridlines
	 */
	Directions getDirections();

	/**
	 * @param line
	 * @return specific gridline
	 */
	GridLine getGridLine(int line);

	/**
	 * @return type of grid
	 */
	GridType getGridType();

	int getLowerLimit(int line, int width, int height);

	int getLowerLimit(int line, int x, int y, int width, int height);

	/**
	 * @return points which determines this grid
	 */
	Point[] getPoints();

	int getUpperLimit(int line, int width, int height);

	int getUpperLimit(int line, int x, int y, int width, int height);
}
