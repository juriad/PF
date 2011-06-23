package pf.board;

import pf.analytics.Point;
import pf.graph.DirectionImpl;

public class DiagonalXGrid extends DiagonalGrid {

	public DiagonalXGrid(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);
	}

	@Override
	protected void addLinesAndDirections() {
		GridLine l;
		Point p3x = p1.move(p3.vectorTo(p1));
		addGridLine(l = new GridLineImpl(p1, p2, p3));
		addDirection(new DirectionImpl(l.getBaseVector()));
		addGridLine(l = new GridLineImpl(p1, p3, p2));
		addDirection(new DirectionImpl(l.getBaseVector()));
		addGridLine(l = new GridLineImpl(p2, p3, p3x));
		addDirection(new DirectionImpl(l.getBaseVector()));
		addGridLine(l = new GridLineImpl(p2, p3x, p3));
		addDirection(new DirectionImpl(l.getBaseVector()));
	}

	@Override
	public GridType getGridType() {
		return GridType.DIAGONALX;
	}

	@Override
	protected boolean shouldIntersect(GridLine gl1, GridLine gl2) {
		return super.shouldIntersect(gl1, gl2) || gl1.equals(getGridLine(2))
				&& gl2.equals(getGridLine(3)) || gl1.equals(getGridLine(3))
				&& gl2.equals(getGridLine(2));
	}

}
