package pf.board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import pf.graph.Edge;
import pf.graph.Graph;
import pf.graph.GraphImpl;
import pf.graph.Vertex;

public class BoardGraph extends GraphImpl {

	public BoardGraph() {
		super(null);
	}

	@Override
	public void setParent(Graph parent) {
		throw new UnsupportedOperationException();
	}

	private void clear() {
		Set<Graph> gs = new HashSet<Graph>();
		for (Graph g : getSubGraphs()) {
			gs.add(g);
		}
		for (Graph g : gs) {
			removeSubGraph(g);
		}
	}

	void makeComponents() {
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

}
