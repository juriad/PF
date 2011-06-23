package pf.board;

import pf.graph.Directions;
import pf.graph.Graph;

public interface Grid {
	Graph createGraph(int width, int height);

	Directions getDirections();

	GridLine getGridLine(int line);

	GridType getGridType();

	int getLowerLimit(int line, int width, int height);

	int getUpperLimit(int line, int width, int height);
}
