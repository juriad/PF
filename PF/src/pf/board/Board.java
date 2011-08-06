package pf.board;

import java.io.File;
import java.io.IOException;

import pf.analytics.Point;
import pf.graph.Vertex;
import pf.interactive.GameBoard;

/**
 * Represents a board which contains points connected by lines in a regular
 * grid.
 * <p>
 * Points and lines are represented by a {@link BoardGraph}.
 * <p>
 * This works as a model for {@link GameBoard}
 * 
 * @author Adam Juraszek
 * 
 */
public interface Board {
	/**
	 * @return graph representing relations on the board
	 */
	BoardGraph getGraph();

	/**
	 * @return grid on this board
	 */
	Grid getGrid();

	/**
	 * @return height of this board
	 */
	int getHeight();

	/**
	 * Very similar to {@link #getNearest(int, int)}, used for snapping
	 * 
	 * @param x
	 * @param y
	 * @return nearest vertex to [x,y]
	 */
	Vertex getNearest(float x, float y);

	/**
	 * @see #getNearest(Point)
	 * @param x
	 * @param y
	 * @return nearest vertex to point [x,y]
	 */
	Vertex getNearest(int x, int y);

	/**
	 * @param p
	 *            point which may or may not be a vertex
	 * @return nearest vertex to p
	 */
	Vertex getNearest(Point p);

	/**
	 * @see #getVertex(Point)
	 * @param x
	 * @param y
	 * @return vertex with the same location as point [x,y]
	 */
	Vertex getVertex(int x, int y);

	/**
	 * @param p
	 *            point which represents some vertex
	 * @return vertex with the same location as p
	 */
	Vertex getVertex(Point p);

	/**
	 * @return width of this board
	 */
	int getWidth();

	/**
	 * Saves this board to file. The way, how graph on this board is represented
	 * in file, is determined by pattern
	 * 
	 * @see GridPattern
	 * @param file
	 * @param pattern
	 * @throws IOException
	 */
	void save(File file, GridPattern pattern) throws IOException;

}
