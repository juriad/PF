package pf.gui;

import pf.analytics.Point;
import pf.board.Board;

public interface GameBoard {
	Board getBoard();

	GridPainter getGridPainter();

	int getHeight();

	VerticesPainter getVerticesPainter();

	int getWidth();

	boolean isPaintGrid();

	boolean isPaintVertices();

	void repaint();

	// TODO preffered size

	void setGridPainter(GridPainter painter);

	void setPaintGrid(boolean paint);

	void setPaintVertices(boolean paint);

	void setVerticesPainter(VerticesPainter painter);

	Point translateFromRead(Point p);

	Point translateToRead(Point p);
}
