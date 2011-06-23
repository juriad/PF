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
import pf.graph.Graph;
import pf.graph.Vertex;

public class BoardImpl implements Board {

	private static class FileLine1 {
		final String stype;
		final GridType type;
		final int width;
		final int height;
		final String sform;
		final Form form;
		final Point[] points;

		public FileLine1(String line) {
			Scanner s = new Scanner(line);
			stype = s.next();
			type = GridType.getType(stype);
			if (type == null)
				throw new InputMismatchException();
			width = s.nextInt();
			height = s.nextInt();
			sform = s.next();
			form = Form.getForm(sform);
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
				throw new InputMismatchException();
			}
		}
	}

	private enum Form {
		FREE("free"),
		REGULAR("regular");

		private final String desc;

		private Form(String desc) {
			this.desc = desc;
		}

		public static Form getForm(String desc) {
			for (Form f : values())
				if (f.getDesc().equals(desc))
					return f;
			return null;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static Board createBoard(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		String line = s.nextLine();
		FileLine1 fl1 = new FileLine1(line);
		System.out.println(fl1.type);
		System.out.println(fl1.width + " " + fl1.height);
		System.out.println(fl1.form);
		System.out.println(Arrays.toString(fl1.points));
		Grid grid = AbstractGrid.createGrid(fl1.type, fl1.points[0],
				fl1.points[1], fl1.points[2]);
		BoardImpl b = new BoardImpl(grid, fl1.width, fl1.height);
		return b;
		//b.graph = grid.createGraph(fl1.width, fl1.height);

		//s.close();
		//return b;
	}

	private Graph graph;
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
		return vs.get(y * width + x);
	}

	@Override
	public Vertex getVertex(Point p) {
		return getVertex(p.getX(), p.getY());
	}

	@Override
	public int getWidth() {
		return width;
	}

}
