package pf.board;

import pf.analytics.Point;
import pf.graph.DirectionImpl;

public class DiagonalGrid extends AbstractGrid {

	public DiagonalGrid(Point p1, Point p2, Point p3) {
		super(p1, p2, p3);

	}

	@Override
	public GridType getGridType() {
		return GridType.DIAGONAL;
	}

	@Override
	protected void addLinesAndDirections() {
		GridLine l;
		addGridLine(l = new GridLineImpl(p1, p2, p3));
		addDirection(new DirectionImpl(getDx(l), getDy(l)));
		addGridLine(l = new GridLineImpl(p1, p3, p2));
		addDirection(new DirectionImpl(getDx(l), getDy(l)));
		addGridLine(l = new GridLineImpl(p2, p3, p1));
		addDirection(new DirectionImpl(getDx(l), getDy(l)));
		addGridLine(l = new GridLineImpl(p1, p2.move(p1.vectorTo(p3)), p2));
		addDirection(new DirectionImpl(getDx(l), getDy(l)));
	}

	@Override
	protected boolean shouldIntersect(GridLine gl1, GridLine gl2) {
		return gl1.equals(getGridLine(0)) && gl2.equals(getGridLine(1))
				|| gl1.equals(getGridLine(1)) && gl2.equals(getGridLine(0));
	}
}
