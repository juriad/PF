package pf.board;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import pf.analytics.Point;

public abstract class AbstractBoardPattern implements BoardPattern {

	public static BoardPattern createBoardPattern(Board board, GridPattern gp,
			File f) {
		if (gp.isSimple()) {
			return SimpleBoardPattern.createSimpleBoardPattern(board, gp);
		}
		return ComplexBoardPattern.createComplexBoardPattern(board, f, gp);
	}

	public static BoardPattern createBoardPattern(Board board, GridPattern gp,
			Set<PointsEdge> pes) {
		if (gp.isSimple()) {
			return SimpleBoardPattern.createSimpleBoardPattern(board, gp, pes);
		}
		return ComplexBoardPattern.createComplexBoardPattern(board, pes, gp);
	}

	protected final Set<PointsEdge> pes;
	private final Board board;

	public AbstractBoardPattern(Board board) {
		this(board, new HashSet<PointsEdge>());
	}

	public AbstractBoardPattern(Board board, Set<PointsEdge> pes) {
		this.pes = pes;
		this.board = board;
	}

	protected void addEdge(Point p1, Point p2) {
		addEdge(p1, p2, false);
	}

	protected void addEdge(Point p1, Point p2, boolean b) {
		pes.add(new PointsEdge(p1, p2, b));
	}

	public Board getBoard() {
		return board;
	}

	@Override
	public Iterator<PointsEdge> iterator() {
		return pes.iterator();
	}
}
