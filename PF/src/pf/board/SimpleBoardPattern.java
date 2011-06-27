package pf.board;

import java.util.Iterator;

import pf.graph.Direction;
import pf.graph.Edge;
import pf.graph.Vertex;

public abstract class SimpleBoardPattern extends AbstractBoardPattern {

	public static class EditBoardPattern extends SimpleBoardPattern {

		public EditBoardPattern(Board board) {
			super(board);
		}

		@Override
		protected void calculateEdges(Board board) {
			for (PointsEdge ep : new FullBoardPattern(board)) {
				Vertex v1 = board.getVertex(ep.p1);
				Vertex v2 = board.getVertex(ep.p2);
				Edge e;
				if (v1 != null && v2 != null) {
					e = v1.edgeToVertex(v2);
				} else {
					e = null;
				}
				addEdge(ep.p1, ep.p2, e != null ? true : false);
			}
		}
	}

	public static class EmptyBoardPattern extends SimpleBoardPattern {

		public EmptyBoardPattern(Board board) {
			super(board);
		}

		@Override
		protected void calculateEdges(Board board) {
		}

	}

	public static class FullBoardPattern extends SimpleBoardPattern {

		public FullBoardPattern(Board board) {
			super(board);
		}

		@Override
		protected void calculateEdges(Board board) {
			Iterator<Vertex> vi = board.getGraph().verticesIterator();
			while (vi.hasNext()) {
				Vertex v = vi.next();
				for (Direction d : board.getGrid().getDirections()) {
					Vertex vv = board
							.getVertex(v.toPoint().move(d.getVector()));
					if (vv != null) {
						addEdge(v.toPoint(), vv.toPoint());
					}
				}
			}
		}
	}

	public static class ShowBoardPattern extends SimpleBoardPattern {

		public ShowBoardPattern(Board board) {
			super(board);
		}

		@Override
		protected void calculateEdges(Board board) {
			Iterator<Edge> ei = board.getGraph().edgesIterator(false);
			while (ei.hasNext()) {
				Edge e = ei.next();
				if (e.isUsed()) {
					addEdge(e.getV1().toPoint(), e.getV2().toPoint());
				}
			}
		}
	}

	public static BoardPattern createSimpleBoardPattern(Board board,
			GridPattern gp) {
		switch (gp) {
		case INTERACTIVE_EDIT:
			return new EditBoardPattern(board);
		case INTERACTIVE_SHOW:
			return new ShowBoardPattern(board);
		case SIMPLE_EMPTY:
			return new EmptyBoardPattern(board);
		case SIMPLE_FULL:
			return new FullBoardPattern(board);
		case SIMPLE_HEXAGONAL:
			break;
		case SIMPLE_PARALLEL:
			break;
		case SIMPLE_SQUARE:
			break;
		default:
			throw new IllegalArgumentException();
		}
		// TODO simple board pattern
		return null;
	}

	public SimpleBoardPattern(Board board) {
		super();
		calculateEdges(board);
	}

	protected abstract void calculateEdges(Board board);
}
