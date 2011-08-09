package pf.animator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pf.board.BoardGraph;
import pf.graph.DirectionImpl;
import pf.graph.Edge;
import pf.graph.EdgeImpl;
import pf.graph.Graph;
import pf.graph.Path;
import pf.graph.PathImpl;
import pf.graph.Vertex;

/**
 * @author Adam Juraszek
 * 
 */
public class EulerPaths {

	/**
	 * Calculates list of paths which together covers all edges in graph. Each
	 * edge is used exactly once and count of paths is the least possible.
	 * <p>
	 * First it makes each graph eulerian, then it calculates eulerian path and
	 * finaly it cuts this path into smaller paths.
	 * 
	 * @param bg
	 * @return list of paths
	 */
	public static List<Path> getEulerPaths(BoardGraph bg) {
		synchronized (bg) {
			int pc = getPathsCount(bg);
			if (pc == 0) {
				return new ArrayList<Path>();
			}

			List<Edge> odds = makeEuler(bg);
			bg.unuseAll();
			Path p = onePath(bg.verticesIterator().next());

			for (Edge e : odds) {
				e.getV1().remove(e);
				e.getV2().remove(e);
			}
			return splitPath(p);
		}
	}

	/**
	 * Calculates count of paths needed to cover all edges.
	 * 
	 * @param bg
	 * @return count of paths
	 */
	public static int getPathsCount(BoardGraph bg) {
		bg.makeComponents();

		Iterator<Graph> gi = bg.subGraphsIterator();
		int paths = 0;
		while (gi.hasNext()) {
			Graph g = gi.next();
			Iterator<Vertex> vi = g.verticesIterator();
			int odds = 0;
			while (vi.hasNext()) {
				Vertex v = vi.next();
				if (v.getDegree(false) % 2 == 1) {
					odds++;
				}
			}
			if (odds == 0) {
				paths++;
			} else {
				paths += odds / 2;
			}
		}
		return paths;
	}

	/**
	 * Calculates circuit paths from and to vertex v.
	 * 
	 * @param v
	 * @return collection of paths
	 */
	private static Collection<? extends Iterator<Edge>> createCircuits(Vertex v) {
		List<Iterator<Edge>> ps = new ArrayList<Iterator<Edge>>();
		while (v.getDegree(true) > 0) {
			ps.add(makePath(v).iterator());
		}
		return ps;
	}

	/**
	 * Determines whether specified edge e is a worm hole.
	 * 
	 * @param e
	 * @return whether e is worm hole
	 */
	private static boolean isOdd(Edge e) {
		int dx = e.getDirection(e.getV1()).getDx();
		return dx == Integer.MAX_VALUE || dx == -Integer.MAX_VALUE;
	}

	/**
	 * Adds extra edges to graph to connect all components and make all vertices
	 * even-degree.
	 * <p>
	 * First it collects all odd-degree vertices in a component. Next, it
	 * connects nearly all of them by a worm hole. Only two odd-degree vertices
	 * in each component should stay, if a component doesn't contain any
	 * odd-degree vertices, it adds one vertex to the list twice.
	 * <p>
	 * In a next step, it connects each component to the next one. If there is
	 * only one component, create the last worm hole unless both vertices are
	 * equal.
	 * <p>
	 * This uses minimum extra edges to make a graph eulerian.
	 * 
	 * @param bg
	 * @return set of extra edges
	 */
	private static List<Edge> makeEuler(BoardGraph bg) {
		List<Edge> set = new ArrayList<Edge>();
		if (bg.getSubGraphs().size() == 0) {
			return set;
		}
		List<Vertex> odds = new ArrayList<Vertex>();
		Iterator<Graph> gi = new RandomizedIterator<Graph>(
				bg.subGraphsIterator());
		while (gi.hasNext()) {
			Graph g = gi.next();
			List<Vertex> vs = new ArrayList<Vertex>();
			Iterator<Vertex> vi = new RandomizedIterator<Vertex>(
					g.verticesIterator());

			Vertex v = null;
			while (vi.hasNext()) {
				v = vi.next();
				if (v.getDegree(false) % 2 == 1) {
					vs.add(v);
				}
			}
			if (vs.size() == 0) {
				vs.add(v);
				vs.add(v);
			}

			for (int i = 0; i < vs.size() - 2; i += 2) {
				Vertex v1 = vs.get(i);
				Vertex v2 = vs.get(i + 1);
				Edge e = new EdgeImpl(v1, v2, new DirectionImpl(
						Integer.MAX_VALUE, set.size()));
				v1.add(e);
				v2.add(e);
				set.add(e);
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
			Edge e = new EdgeImpl(v1, v2, new DirectionImpl(Integer.MAX_VALUE,
					set.size()));
			v1.add(e);
			v2.add(e);
			set.add(e);
		}
		return set;
	}

	/**
	 * Graph theory ensures there is always a circuit in a eulerian graph.
	 * <p>
	 * This iterates over unused edges in vertex and adds random such edge to
	 * path and moves on.
	 * 
	 * @param v
	 *            start vertex of path
	 * @return path which is a circuit
	 */
	private static Path makePath(Vertex v) {
		Path path = new PathImpl(v);
		Edge e = null;
		while ((v = path.getLastVertex()).getDegree(true) > 0) {
			Iterator<Edge> eei = new RandomizedIterator<Edge>(
					v.edgesIterator(true));
			e = eei.next();
			path.extend(e);
			e.setUsed(true);
		}
		return path;
	}

	/**
	 * Uses hidden recurrence implemented as a stack.
	 * <p>
	 * For each visited vertex it calculates all circuits going through this
	 * vertex and follows each of them before it moves along the original
	 * circuit.
	 * 
	 * @param v
	 *            start vertex
	 * @return one path covering whole eulerian graph
	 */
	private static Path onePath(Vertex v) {
		Path p = new PathImpl(v);
		List<Iterator<Edge>> ps = new ArrayList<Iterator<Edge>>();

		Collection<? extends Iterator<Edge>> c = createCircuits(v);
		ps.addAll(c);
		while (ps.size() > 0) {
			while (ps.get(ps.size() - 1).hasNext()) {
				Edge e = ps.get(ps.size() - 1).next();
				p.extend(e);
				v = e.getOther(v);
				c = createCircuits(v);
				ps.addAll(c);
			}
			ps.remove(ps.size() - 1);
		}
		return p;
	}

	/**
	 * A valid path is between two worm hole segments, its length is at least
	 * one segment. Moreover the last path can continue by the first few
	 * segments.
	 * 
	 * @param p
	 *            one path
	 * @return list of smaller paths
	 */
	private static List<Path> splitPath(Path p) {
		List<Path> paths = new ArrayList<Path>();
		Iterator<Edge> ei = p.iterator();
		Iterator<Vertex> vi = p.verticesIterator();
		Vertex v = vi.next();
		boolean oddUnseen = true;
		Path untilOdd = new PathImpl(v);
		Path current = null;
		while (ei.hasNext()) {
			Edge e = ei.next();
			v = vi.next();
			if (oddUnseen) {
				if (isOdd(e)) {
					oddUnseen = false;
				} else {
					untilOdd.extend(e);
				}
			} else {
				if (isOdd(e)) {
					if (current.length() > 0) {
						paths.add(current);
					}
				} else {
					current.extend(e);
				}
			}
			if (isOdd(e)) {
				current = new PathImpl(v);
			}
		}
		current.extend(untilOdd);
		if (current.length() > 0) {
			paths.add(current);
		}
		return paths;
	}

	/**
	 * Library class
	 */
	private EulerPaths() {
	}

}
