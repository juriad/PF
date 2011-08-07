package pf.board;

import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.Set;

import pf.analytics.LineImpl;
import pf.analytics.Vector;
import pf.graph.Direction;
import pf.graph.Edge;
import pf.graph.Vertex;

/**
 * Groups all simple patterns, they are not rich enough to store something more
 * complex.
 * 
 * @author Adam Juraszek
 * 
 */
public abstract class SimpleBoardPattern extends AbstractBoardPattern {

	/**
	 * Simple board pattern which transforms show mode board into an edit mode
	 * board
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class EditBoardPattern extends SimpleBoardPattern {

		public EditBoardPattern(Board board) {
			super(board);
		}

		public EditBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected void calculateEdges() {
			for (PointsEdge ep : new FullBoardPattern(getBoard())) {
				Vertex v1 = getBoard().getVertex(ep.p1);
				Vertex v2 = getBoard().getVertex(ep.p2);
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

	/**
	 * Simple board pattern which represents an empty board
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class EmptyBoardPattern extends SimpleBoardPattern {

		public EmptyBoardPattern(Board board) {
			super(board);
		}

		public EmptyBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) {
		}

		@Override
		protected void calculateEdges() {
		}

	}

	/**
	 * Simple board pattern which represents a full board
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class FullBoardPattern extends SimpleBoardPattern {

		public FullBoardPattern(Board board) {
			super(board);
		}

		public FullBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) {
		}

		private boolean isEdge(Vertex v, Vertex vv) {
			switch (board.getGrid().getGridType()) {
			case DIAGONAL:
			case SQUARE:
			case TRIANGLE:
				return true;
			case DIAGONALX:
				// if intersection of grid line 2,3
				Vector v1 = board.getGrid().getGridLine(2).baseVector();
				Vector v2 = board.getGrid().getGridLine(3).baseVector();
				int det = LineImpl.det(v1.getX(), v1.getY(), v2.getX(),
						v2.getY());
				int detx = LineImpl.det(v.getX(), v1.getY(), v.getX(),
						v2.getY());
				int dety = LineImpl.det(v1.getX(), v.getY(), v2.getX(),
						v.getY());
				if (detx % det == 0 && dety % det == 0) {
					// intersection +
					return true;
				}
				// intersection x
				return false;
			}
			return false;
		}

		@Override
		protected void calculateEdges() {
			Iterator<Vertex> vi = getBoard().getGraph().verticesIterator();
			while (vi.hasNext()) {
				Vertex v = vi.next();
				for (Direction d : getBoard().getGrid().getDirections()) {
					Vertex vv = getBoard().getVertex(v.getX() + d.getDx(),
							v.getY() + d.getDy());
					if (vv != null) {
						if (isEdge(v, vv)) {
							addEdge(v, vv);
						}
					}
				}
			}
		}
	}

	/**
	 * Simple board pattern which transforms edit mode board into show mode
	 * board
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	public static class ShowBoardPattern extends SimpleBoardPattern {

		public ShowBoardPattern(Board board) {
			super(board);
		}

		public ShowBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected void calculateEdges() {
			Iterator<Edge> ei = getBoard().getGraph().edgesIterator(false);
			while (ei.hasNext()) {
				Edge e = ei.next();
				if (e.isUsed()) {
					addEdge(e.getV1(), e.getV2());
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
		default:
			throw new IllegalArgumentException();
		}
	}

	public static BoardPattern createSimpleBoardPattern(Board board,
			GridPattern gp, Set<PointsEdge> pes) {
		switch (gp) {
		case INTERACTIVE_EDIT:
			return new EditBoardPattern(board, pes);
		case INTERACTIVE_SHOW:
			return new ShowBoardPattern(board, pes);
		case SIMPLE_EMPTY:
			return new EmptyBoardPattern(board, pes);
		case SIMPLE_FULL:
			return new FullBoardPattern(board, pes);
		default:
			throw new IllegalArgumentException();
		}
	}

	public SimpleBoardPattern(Board board) {
		super(board);
		calculateEdges();
	}

	public SimpleBoardPattern(Board board, Set<PointsEdge> pes) {
		super(board, pes);
	}

	protected abstract void calculateEdges();
}
