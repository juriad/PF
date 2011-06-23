package pf.board;

import pf.analytics.Point;
import pf.graph.Graph;
import pf.graph.Vertex;

public interface Board {
	Graph getGraph();

	Grid getGrid();

	int getHeight();

	Vertex getNearest(int x, int y);

	Vertex getNearest(Point p);

	Vertex getVertex(int x, int y);

	Vertex getVertex(Point p);

	int getWidth();

}
