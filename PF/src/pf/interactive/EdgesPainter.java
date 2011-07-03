package pf.interactive;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import pf.graph.Edge;

public interface EdgesPainter {

	Rectangle getBounds(GameBoard gameBoard, Edge e);

	void paintEdges(Graphics2D g2d, GameBoard gameBoard);

}
