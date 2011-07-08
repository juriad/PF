package pf.interactive;

import java.awt.Graphics2D;

import pf.reimpl.Path2D;

public interface PathPainter {

	Path2D<?> createPath2D();

	PathPainterFactory getFactory();

	void paintPath(Graphics2D g2d, Path2D<?> p2d);

}
