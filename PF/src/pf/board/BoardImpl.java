package pf.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import pf.analytics.Line;
import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.board.BoardPattern.PointsEdge;
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
		System.out.println(fh.type);
		System.out.println(fh.width + " " + fh.height);
		System.out.println(fh.form);
		System.out.println(Arrays.toString(fh.points));
		Grid grid = AbstractGrid.createGrid(fh.type, fh.points[0],
				fh.points[1], fh.points[2]);
		BoardImpl b = new BoardImpl(grid, fh.width, fh.height);
		b.graph = grid.createGraph(fh.width, fh.height);
		BoardPattern bp = AbstractBoardPattern.createBoardPattern(b,
				fh.pattern, f);
		for (PointsEdge pe : bp)
			System.out.println(pe);

		return b;
	}

	private BoardGraph graph;
	private final Grid grid;
	private final int height;
	protected final Map<Integer, Vertex> vs;

	private final int width;

	public BoardImpl(Grid grid, int width, int height) {
		this.grid = grid;
		this.width = width;
		this.height = height;
		vs = new HashMap<Integer, Vertex>();
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
		return graph.getVertex(p);
	}

	@Override
	public int getWidth() {
		return width;
	}

}
