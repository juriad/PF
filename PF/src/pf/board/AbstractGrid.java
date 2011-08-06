package pf.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pf.analytics.Line;
import pf.analytics.LineImpl;
import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.graph.Direction;
import pf.graph.Directions;
import pf.graph.DirectionsImpl;
import pf.graph.Vertex;
import pf.graph.VertexImpl;

/**
 * Abstract grid provides basic implementation of most methods.
 * <p>
 * {@link #createGrid(GridType, Point, Point, Point)} is a root method for
 * constructing grid. It calls particular equivalents in subclasses.
 * 
 * @author Adam Juraszek
 * 
 */
public abstract class AbstractGrid implements Grid {

	public static Grid createGrid(GridType type, Point p1, Point p2, Point p3) {
		switch (type) {
		case TRIANGLE:
			return new TriangleGrid(p1, p2, p3);
		case SQUARE:
			return new SquareGrid(p1, p2, p3);
		case DIAGONAL:
			return new DiagonalGrid(p1, p2, p3);
		case DIAGONALX:
			return new DiagonalXGrid(p1, p2, p3);
		default:
			throw new IllegalStateException();
		}
	}

	private final DirectionsImpl ds;

	private final List<GridLine> lines;

	protected final Point p1, p2, p3;

	public AbstractGrid(Point p1, Point p2, Point p3) {
		if (p1.equals(p2)) {
			throw new IllegalArgumentException();
		}
		if ((new LineImpl(p1, p2)).contains(p3)) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		lines = new ArrayList<GridLine>();
		ds = new DirectionsImpl();
		addLinesAndDirections();
		if (lines.size() != getGridType().getLines()
				|| ds.getDirections().size() != getGridType().getLines() * 2) {
			throw new IllegalStateException();
		}
	}

	@Override
	public BoardGraph createGraph(int width, int height) {
		return generateVertices(width, height);
	}

	@Override
	public Directions getDirections() {
		return ds;
	}

	@Override
	public GridLine getGridLine(int line) {
		return lines.get(line);
	}

	@Override
	public int getLowerLimit(int line, int width, int height) {
		return getLowerLimit(line, 0, 0, width, height);
	}

	@Override
	public int getLowerLimit(int line, int x, int y, int width, int height) {
		GridLine gl = getGridLine(line);
		double min = Double.POSITIVE_INFINITY;
		for (Point p : getCorners(x, y, width, height)) {
			min = Math.min(min, gl.getNearest(p));
		}
		return (int) Math.floor(min);
	}

	@Override
	public Point[] getPoints() {
		return new Point[] { p1, p2, p3 };
	}

	@Override
	public int getUpperLimit(int line, int width, int height) {
		return getUpperLimit(line, 0, 0, width, height);
	}

	@Override
	public int getUpperLimit(int line, int x, int y, int width, int height) {
		GridLine gl = getGridLine(line);
		double max = Double.NEGATIVE_INFINITY;
		for (Point p : getCorners(x, y, width, height)) {
			max = Math.max(max, gl.getNearest(p));
		}
		return (int) Math.ceil(max);
	}

	private Set<Point> getCorners(int x, int y, int width, int height) {
		Set<Point> c = new HashSet<Point>();
		c.add(new PointImpl(x, y));
		c.add(new PointImpl(x + width, y));
		c.add(new PointImpl(x, y + height));
		c.add(new PointImpl(x + width, y + height));
		return c;
	}

	protected void addDirection(Direction d) {
		ds.addDirection(d);
		ds.addDirection(d.getOpposite());
	}

	protected void addGridLine(GridLine l) {
		lines.add(l);
	}

	protected abstract void addLinesAndDirections();

	protected BoardGraph generateVertices(int width, int height) {
		int[] mins = new int[getGridType().getLines()];
		int[] maxs = new int[getGridType().getLines()];
		for (int i = 0; i < mins.length; i++) {
			mins[i] = getLowerLimit(i, width, height);
			maxs[i] = getUpperLimit(i, width, height);
		}

		BoardGraph g = new BoardGraph();
		Set<Point> ps = new HashSet<Point>();

		Point pp = null;
		GridLine gl1, gl2;
		Line l1, l2;
		for (int i = 0; i < getGridType().getLines(); i++) {
			gl1 = getGridLine(i);
			for (int j = i + 1; j < getGridType().getLines(); j++) {
				gl2 = getGridLine(j);
				for (int ii = mins[i]; ii <= maxs[i]; ii++) {
					if (!shouldIntersect(gl1, gl2)) {
						continue;
					}
					l1 = gl1.getLine(ii);
					for (int jj = mins[j]; jj <= maxs[j]; jj++) {
						l2 = gl2.getLine(jj);
						pp = l1.intersection(l2);
						if (pp != null
								&& pp.isInside(PointImpl.O, new PointImpl(
										width, height))) {
							if (!ps.contains(pp)) {
								Vertex v = new VertexImpl(g, pp.getX(),
										pp.getY());
								g.addSubGraph(v);
								ps.add(pp);
							}
						}
					}
				}
			}
		}
		return g;
	}

	protected final int getDx(Line l) {
		return l.getP2().getX() - l.getP1().getX();
	}

	protected final int getDy(Line l) {
		return l.getP2().getY() - l.getP1().getY();
	}

	/**
	 * Whether intersection of following two gridlines makes a vertex
	 * 
	 * @param gl1
	 * @param gl2
	 * @return
	 */
	protected abstract boolean shouldIntersect(GridLine gl1, GridLine gl2);
}
