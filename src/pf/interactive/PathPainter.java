package pf.interactive;

import java.awt.Graphics2D;

/**
 * Paints paths. This class doesn't control the way how the paths are painted.
 * 
 * @author Adam Juraszek
 * 
 */
public interface PathPainter {

	/**
	 * @return {@link Path2D} which contains path translated to screen
	 *         coordinates
	 */
	Path2D<?> createPath2D();

	/**
	 * Paints path defined by Path2D in screen coordinates
	 * 
	 * @param g2d
	 * @param p2d
	 */
	void paintPath(Graphics2D g2d, Path2D<?> p2d);

}
