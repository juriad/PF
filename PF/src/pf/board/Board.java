package pf.board;

import java.io.File;
import java.io.IOException;

import pf.analytics.Point;
import pf.graph.Vertex;

public interface Board {
	BoardGraph getGraph();

	Grid getGrid();

	int getHeight();

	Vertex getNearest(float x, float y);

	Vertex getNearest(int x, int y);

	Vertex getNearest(Point p);

	Vertex getVertex(int x, int y);

	Vertex getVertex(Point p);

	int getWidth();

	void save(File file, GridPattern pattern) throws IOException;

}
