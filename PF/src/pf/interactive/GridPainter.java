package pf.interactive;

import java.awt.Graphics2D;

/**
 * Paints grid. This class doesn't control the way how the grid is painted.
 * 
 * @author Adam Juraszek
 * 
 */
public interface GridPainter {

	/**
	 * Paints whole grid in rectangle specified by g2d
	 * 
	 * @param g2d
	 * @param gameBoard
	 */
	void paintGrid(Graphics2D g2d, GameBoard gameBoard);

}
