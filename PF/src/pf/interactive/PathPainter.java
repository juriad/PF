package pf.interactive;

import java.awt.Graphics2D;

public interface PathPainter {

	Path2D<?> createPath2D();

	void paintPath(Graphics2D g2d, Path2D<?> p2d);

}
