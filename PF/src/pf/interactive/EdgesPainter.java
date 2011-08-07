package pf.interactive;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import pf.graph.Edge;

/**
 * Paints edges. This class doesn't control the way how the edges are painted.
 * 
 * @author Adam Juraszek
 * 
 */
public interface EdgesPainter {

	/**
	 * Calculates rectangle to be redrawn by e.
	 * 
	 * @param gameBoard
	 * @param e
	 * @return
	 */
	Rectangle getBounds(GameBoard gameBoard, Edge e);

	/**
	 * Paints all edges in rectangle specified by g2d
	 * 
	 * @param g2d
	 * @param gameBoard
	 */
	void paintEdges(Graphics2D g2d, GameBoard gameBoard);

}
