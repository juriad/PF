package pf.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import pf.analytics.Line;
import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.graph.Graph;
import pf.graph.Vertex;

public class BoardImpl implements Board {

	public static Board createBoard(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		s.useDelimiter("[\\n ]\\s*[\\],\\[]*\\s*");
		String ss = s.next();
		System.out.println(ss);
		GridType type = GridType.getType(ss);
		int width = s.nextInt();
		int height = s.nextInt();
		ss = s.next();
		Grid grid;
		System.out.println(ss);
		if (ss.equals("regular")) {
			grid = AbstractGrid.createRegularGrid(type);
		} else if (ss.equals("free")) {
			Point p1 = new PointImpl(s.nextInt(), s.nextInt());
			Point p2 = new PointImpl(s.nextInt(), s.nextInt());
			Point p3 = new PointImpl(s.nextInt(), s.nextInt());
			grid = AbstractGrid.createGrid(type, p1, p2, p3);
		} else {
			throw new IllegalStateException();
		}
		s.close();

		System.out.println(width+" "+height);
		BoardImpl b = new BoardImpl(grid, width, height);
		b.graph = grid.createGraph(width, height);
		return b;
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
		for (int i = 0; i < grid.getGridType().getLines(); i++) {
			pars[i] = grid.getGridLine(i).getNearestLine(p);
		}

		Point pp = null;
		Vertex v = null;
		int dist = Integer.MAX_VALUE;
		for (int i = 0; i < pars.length; i++) {
			for (int j = i + 1; j < pars.length; j++) {
				pp = pars[i].intersection(pars[j]);
				if (pp != null && getVertex(pp) != null) {
					if (dist > p.vectorTo(pp).lengthSq()) {
						v = getVertex(pp);
						dist = p.vectorTo(pp).lengthSq();
					}
				}
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
