package pf.interactive;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import pf.graph.Vertex;

public interface VerticesPainter {

	Rectangle getBounds(GameBoard board, Vertex v);

	void paintVertices(Graphics2D g2d, GameBoard gameBoard);

}
