package pf.interactive;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import pf.graph.Vertex;

/**
 * Paints vertices. This class doesn't control the way how the vertices are
 * painted.
 * 
 * @author Adam Juraszek
 * 
 */
public interface VerticesPainter {

	/**
	 * Calculates rectangle to be redrawn by v.
	 * 
	 * @param gameBoard
	 * @param e
	 * @return
	 */
	Rectangle getBounds(GameBoard board, Vertex v);

	/**
	 * Paints all vertices in rectangle specified by g2d
	 * 
	 * @param g2d
	 * @param gameBoard
	 */
	void paintVertices(Graphics2D g2d, GameBoard gameBoard);

}
