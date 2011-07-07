package pf.interactive;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import pf.graph.Path;

public interface PathPainter {

	public Rectangle getBounds();

	public Rectangle getDiffBounds();

	PathPainterFactory getFactory();

	void paintPath(Graphics2D g2d, InteractiveBoard interactiveBoard, Path p);

}
