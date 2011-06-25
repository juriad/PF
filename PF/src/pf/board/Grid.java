package pf.board;

import pf.graph.Directions;

public interface Grid {
	BoardGraph createGraph(int width, int height);

	Directions getDirections();

	GridLine getGridLine(int line);

	GridType getGridType();

	int getLowerLimit(int line, int width, int height);

	int getLowerLimit(int line, int x, int y, int width, int height);

	int getUpperLimit(int line, int width, int height);

	int getUpperLimit(int line, int x, int y, int width, int height);
}
