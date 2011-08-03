package pf.animator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pf.board.BoardGraph;
import pf.graph.Direction;
import pf.graph.DirectionImpl;
import pf.graph.Edge;
import pf.graph.EdgeImpl;
import pf.graph.Graph;
import pf.graph.Path;
import pf.graph.PathImpl;
import pf.graph.Vertex;

public class EulerPaths {

	public static Path getEulerPaths(BoardGraph bg) {
		Iterator<Edge> ei = bg.edgesIterator(false);
		while (ei.hasNext()) {
			Edge e = ei.next();
			System.out
					.println(e
							+ " "
							+ (Math.abs(e.getDirection(e.getV1()).getDx()) == Integer.MAX_VALUE));
		}
		System.out.println("get euler path");
		bg.makeComponents();
		System.out.println("conponents made");
		System.out.println(bg.getSubGraphs().size());
		List<Edge> odds = makeEuler(bg);
		System.out.println("odds:");
		for (Edge e : odds) {
			System.out.println("odd " + e);
		}
		System.out.println("euler made");
		setUnused(bg);
		System.out.println("set unused");

		// FIXME check empty

		Path p = onePath(bg.verticesIterator().next());
		System.out.println("one path");

		for (Edge e : odds) {
			e.getV1().remove(e);
			e.getV2().remove(e);
		}

		// TODO split path
		return p;
	}

	public static int getPathsCount(BoardGraph bg) {
		// TODO getPathsCount
		return 0;
	}

	private static Collection<? extends Iterator<Edge>> createCircuits(Vertex v) {
		List<Iterator<Edge>> ps = new ArrayList<Iterator<Edge>>();
		while (v.getDegree(true) > 0) {
			ps.add(makePath(v).iterator());
		}
		return ps;
	}

	private static List<Edge> makeEuler(BoardGraph bg) {
		Direction wh = new DirectionImpl(Integer.MAX_VALUE, Integer.MAX_VALUE);
		List<Edge> set = new ArrayList<Edge>();
		if (bg.getSubGraphs().size() == 0) {
			return set;
		}
		List<Vertex> odds = new ArrayList<Vertex>();
		Iterator<Graph> gi = bg.subGraphsIterator();
		while (gi.hasNext()) {
			Graph g = gi.next();
			List<Vertex> vs = new ArrayList<Vertex>();
			Iterator<Vertex> vi = g.verticesIterator();

			Vertex v = null;
			while (vi.hasNext()) {
				v = vi.next();
				if (v.getDegree(false) % 2 == 1) {
					System.out.println("odd found: " + v);
					vs.add(v);
				}
			}
			if (vs.size() == 0) {
				vs.add(v);
				vs.add(v);
				System.out.println("added fake odds: " + v);
			}

			for (int i = 0; i < vs.size() - 2; i += 2) {
				Vertex v1 = vs.get(i);
				Vertex v2 = vs.get(i + 1);
				Edge e = new EdgeImpl(v1, v2, wh);
				v1.add(e);
				v2.add(e);
				set.add(e);
				System.out.println("made odd edge: " + e);
			}
			odds.add(vs.get(vs.size() - 2));
			odds.add(vs.get(vs.size() - 1));
		}
		if (bg.getSubGraphs().size() == 1 && odds.get(0).equals(odds.get(1))) {
			return set;
		}
		for (int i = 0; i < odds.size(); i += 2) {
			Vertex v1 = odds.get(i + 1);
			Vertex v2 = odds.get((i + 2) % odds.size());
			Edge e = new EdgeImpl(v1, v2, wh);
			v1.add(e);
			v2.add(e);
			set.add(e);
		}
		return set;
	}

	private static Path makePath(Vertex v) {
		Path path = new PathImpl(v);
		Edge e = null;
		while ((v = path.getLastVertex()).getDegree(true) > 0) {
			Iterator<Edge> eei = v.edgesIterator(true);
			e = eei.next();
			path.extend(e);
			e.setUsed(true);
		}
		return path;
	}

	private static Path onePath(Vertex v) {
		Path p = new PathImpl(v);
		List<Iterator<Edge>> ps = new ArrayList<Iterator<Edge>>();

		Collection<? extends Iterator<Edge>> c = createCircuits(v);
		ps.addAll(c);
		System.out.println("initial circuits: " + c.size());
		while (ps.size() > 0) {
			while (ps.get(ps.size() - 1).hasNext()) {
				Edge e = ps.get(ps.size() - 1).next();
				System.out.println(e);
				p.extend(e);
				v = e.getOther(v);
				c = createCircuits(v);
				ps.addAll(c);
				System.out.println("new circuits: " + c.size());
			}
			ps.remove(ps.size() - 1);
			System.out.println("circuit removed");
		}
		return p;
	}

	private static void setUnused(BoardGraph bg) {
		Iterator<Edge> ei = bg.edgesIterator(false);
		while (ei.hasNext()) {
			Edge e = ei.next();
			e.setUsed(false);
		}
	}

	private EulerPaths() {
	}

}
