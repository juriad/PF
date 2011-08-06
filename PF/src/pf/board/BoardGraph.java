package pf.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import pf.graph.Edge;
import pf.graph.Graph;
import pf.graph.GraphImpl;
import pf.graph.Vertex;

/**
 * Represents graph on a board. Adds some useful methods and makes most methods
 * synchronized.
 * <p>
 * Synchronization is required because of massive changes in
 * {@link #makeComponents()}, all iterators are made stable.
 * 
 * @author Adam Juraszek
 * 
 */
public class BoardGraph extends GraphImpl {

	public BoardGraph() {
		super(null);
	}

	@Override
	public synchronized boolean addSubGraph(Graph sub) {
		return super.addSubGraph(sub);
	}

	@Override
	public synchronized Iterator<Edge> edgesIterator(boolean used) {
		return getStableIterator(super.edgesIterator(used));
	}

	@Override
	public synchronized Iterator<Edge> edgesIterator(Graph root, boolean used) {
		return getStableIterator(super.edgesIterator(root, used));
	}

	@Override
	public synchronized Set<Graph> getSubGraphs() {
		return super.getSubGraphs();
	}

	/**
	 * Rebuilds this graph. Each new subgraph contains exactly one component.
	 */
	public synchronized void makeComponents() {
		Map<Vertex, Integer> vs = new HashMap<Vertex, Integer>();
		Queue<Vertex> fifo = new LinkedList<Vertex>();
		Iterator<Vertex> vi = verticesIterator();
		while (vi.hasNext()) {
			vs.put(vi.next(), 0);
		}
		clear();

		Graph c;
		int comp = 0;
		for (Vertex v : vs.keySet()) {
			if (vs.get(v) == 0) {
				comp++;
				fifo.offer(v);
				vs.put(v, comp);
				Vertex vv;
				c = new GraphImpl(this);
				addSubGraph(c);
				while (!fifo.isEmpty()) {
					vv = fifo.poll();

					c.addSubGraph(vv);
					Vertex vvv;
					for (Edge e : vv) {
						vvv = e.getOther(vv);
						if (vs.get(vvv) == 0) {
							fifo.offer(vvv);
							vs.put(vvv, comp);
						}
					}
				}
			}
		}
	}

	@Override
	public synchronized boolean removeSubGraph(Graph sub) {
		return super.removeSubGraph(sub);
	}

	/**
	 * This graph is root, so setting parent shouldn't be supported
	 */
	@Override
	public void setParent(Graph parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized Iterator<Graph> subGraphsIterator() {
		return getStableIterator(super.subGraphsIterator());
	}

	/**
	 * Makes all edges unused.
	 */
	public synchronized void unuseAll() {
		Iterator<Edge> ei = edgesIterator(false);
		while (ei.hasNext()) {
			Edge e = ei.next();
			if (e.isUsed()) {
				e.setUsed(false);
			}
		}
	}

	@Override
	public synchronized Iterator<Vertex> verticesIterator() {
		return getStableIterator(super.verticesIterator());
	}

	/**
	 * Clears this graph. After clear() it contains no subgraph.
	 */
	private synchronized void clear() {
		Set<Graph> gs = new HashSet<Graph>();
		for (Graph g : getSubGraphs()) {
			gs.add(g);
		}
		for (Graph g : gs) {
			removeSubGraph(g);
		}
	}

	protected synchronized <E> Iterator<E> getStableIterator(Iterator<E> i) {
		List<E> local = new ArrayList<E>();
		while (i.hasNext()) {
			local.add(i.next());
		}
		return local.iterator();
	}

}
