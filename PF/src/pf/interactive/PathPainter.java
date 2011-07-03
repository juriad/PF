package pf.interactive;

import java.awt.Graphics2D;

import pf.graph.Path;

public interface PathPainter {

	void paintPath(Graphics2D g2d, InteractiveBoard interactiveBoard, Path p);

}
