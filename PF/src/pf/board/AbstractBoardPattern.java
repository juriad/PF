package pf.board;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pf.analytics.Point;

public abstract class AbstractBoardPattern implements BoardPattern {

	public static BoardPattern createBoardPattern(Board board, GridPattern gp,
			File f) {
		if (gp.isSimple()) {

		} else
			return ComplexBoardPattern.createComplexBoardPattern(board, f, gp);
		return null;
	}

	protected final List<PointsEdge> pes;

	public AbstractBoardPattern() {
		pes = new ArrayList<PointsEdge>();
	}

	protected void addEdge(Point p1, Point p2) {
		pes.add(new PointsEdge(p1, p2));
	}

	@Override
	public Iterator<PointsEdge> iterator() {
		return pes.iterator();
	}
}
