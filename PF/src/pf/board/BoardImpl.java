package pf.board;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import pf.analytics.Line;
import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.board.BoardPattern.PointsEdge;
import pf.graph.Edge;
import pf.graph.EdgeImpl;
import pf.graph.Graph;
import pf.graph.Vertex;

public class BoardImpl implements Board {

	private static class FileHeader {
		final String stype;
		final GridType type;
		final int width;
		final int height;
		final String sform;
		final GridForm form;
		final Point[] points;
		final String spattern;
		final GridPattern pattern;

		public FileHeader(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			stype = s.next();
			type = GridType.getType(stype);
			if (type == null)
				throw new InputMismatchException(stype);
			width = s.nextInt();
			height = s.nextInt();
			sform = s.next();
			form = GridForm.getForm(sform);
			switch (form) {
			case FREE:
				points = new Point[3];
				String p;
				p = s.findInLine(Point.pattern);
				points[0] = PointImpl.read(p);
				p = s.findInLine(Point.pattern);
				points[1] = PointImpl.read(p);
				p = s.findInLine(Point.pattern);
				points[2] = PointImpl.read(p);
				break;
			case REGULAR:
				points = type.getRegularPoints();
				break;
			default:
				throw new InputMismatchException(sform);
			}
			s.nextLine();
			spattern = s.nextLine();
			pattern = GridPattern.getPattern(spattern);
			if (pattern == null)
				throw new InputMismatchException(spattern);
		}
	}

	private enum GridForm {
		FREE ("free"),
		REGULAR ("regular");

		public static GridForm getForm(String desc) {
			for (GridForm f : values())
				if (f.getDesc().equals(desc))
					return f;
			return null;
		}

		private final String desc;

		private GridForm(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static Board createBoard(File f) throws FileNotFoundException {
		FileHeader fh = new FileHeader(f);
		Grid grid = AbstractGrid.createGrid(fh.type, fh.points[0],
				fh.points[1], fh.points[2]);
		BoardImpl b = new BoardImpl(grid, fh.width, fh.height);
		b.graph = grid.createGraph(fh.width, fh.height);
		BoardPattern bp = AbstractBoardPattern.createBoardPattern(b,
				fh.pattern, f);
		fillVs(b);
		for (PointsEdge pe : bp)
			createEdge(b, pe);
		b.graph.makeComponents();
		return b;
	}

	private static void createEdge(Board b, PointsEdge pe) {
		Vertex v1 = b.getVertex(pe.p1);
		Vertex v2 = b.getVertex(pe.p2);

		Edge e = new EdgeImpl(v1, v2, b.getGrid().getDirections()
				.getNearestDirection(v1, v2));
		v1.add(e);
		v2.add(e);
	}

	private static void fillVs(BoardImpl b) {
		Iterator<Vertex> vi = b.getGraph().verticesIterator();
		while (vi.hasNext())
			b.addVertex(vi.next());
	}

	private BoardGraph graph;

	private final Grid grid;
	private final int height;
	protected final Map<Point, Vertex> vs;
	private final int width;

	public BoardImpl(Grid grid, int width, int height) {
		this.grid = grid;
		this.width = width;
		this.height = height;
		vs = new HashMap<Point, Vertex>();
	}

	private void addVertex(Vertex v) {
		vs.put(new PointImpl(v.getX(), v.getY()), v);
	}

	@Override
	public Graph getGraph() {
		return graph;
	}

	@Override
	public Grid getGrid() {
		return grid;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Vertex getNearest(float x, float y) {
		Line[] pars = new Line[grid.getGridType().getLines()];
		for (int i = 0; i < grid.getGridType().getLines(); i++) {
			GridLine gl = getGrid().getGridLine(i);
			Line ll1 = gl.getLine(-1);
			Line2D.Float l1 = new Line2D.Float(ll1.getP1().getX(), ll1.getP1()
					.getY(), ll1.getP2().getX(), ll1.getP2().getY());
			Line ll2 = gl.getLine(1);
			Line2D.Float l2 = new Line2D.Float(ll2.getP1().getX(), ll2.getP1()
					.getY(), ll2.getP2().getX(), ll2.getP2().getY());
			l1.ptLineDist(ll2.getP1().getX(), ll2.getP1().getY());

			Line ll0 = gl.getLine(0);
			Line2D.Float l0 = new Line2D.Float(ll0.getP1().getX(), ll0.getP1()
					.getY(), ll0.getP2().getX(), ll0.getP2().getY());

			double dist = l0.ptLineDist(x, y);
			double dist1 = l1.ptLineDist(x, y);
			double dist2 = l2.ptLineDist(x, y);

			dist *= (dist1 < dist2 ? -1 : 1);

			double pard = dist / Math.abs(l0.ptLineDist(l1.getP1()));
			int par = (int) Math.round(pard);

			pars[i] = gl.getLine(par);
		}

		Point pp = null;
		Vertex v = null;
		double dist = Integer.MAX_VALUE;
		for (int i = 0; i < pars.length; i++)
			for (int j = i + 1; j < pars.length; j++) {
				pp = pars[i].intersection(pars[j]);
				if (pp != null && getVertex(pp) != null) {
					Point2D.Float p = new Point2D.Float(pp.getX(), pp.getY());
					if (dist > p.distanceSq(x, y)) {
						v = getVertex(pp);
						dist = p.distanceSq(x, y);
					}
				}
			}
		return v;
	}

	@Override
	public Vertex getNearest(int x, int y) {
		return getNearest(new PointImpl(x, y));
	}

	@Override
	public Vertex getNearest(Point p) {
		Line[] pars = new Line[grid.getGridType().getLines()];
		for (int i = 0; i < grid.getGridType().getLines(); i++)
			pars[i] = grid.getGridLine(i).getNearestLine(p);

		Point pp = null;
		Vertex v = null;
		int dist = Integer.MAX_VALUE;
		for (int i = 0; i < pars.length; i++)
			for (int j = i + 1; j < pars.length; j++) {
				pp = pars[i].intersection(pars[j]);
				if (pp != null && getVertex(pp) != null)
					if (dist > p.vectorTo(pp).lengthSq()) {
						v = getVertex(pp);
						dist = p.vectorTo(pp).lengthSq();
					}
			}
		return v;
	}

	@Override
	public Vertex getVertex(int x, int y) {
		return getVertex(new PointImpl(x, y));
	}

	@Override
	public Vertex getVertex(Point p) {
		return vs.get(p);
	}

	@Override
	public int getWidth() {
		return width;
	}

}
