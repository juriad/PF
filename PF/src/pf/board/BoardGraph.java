package pf.board;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.graph.Graph;
import pf.graph.GraphImpl;
import pf.graph.Vertex;

public class BoardGraph extends GraphImpl {

	protected final Map<Point, Vertex> vs;

	public BoardGraph() {
		super(null);
		vs = new HashMap<Point, Vertex>();
	}

	@Override
	public boolean addSubGraph(Graph sub) {
		boolean ret = super.addSubGraph(sub);
		if (ret) {
			Iterator<Vertex> i = sub.verticesIterator();
			Vertex v;
			while (i.hasNext()) {
				v = i.next();
				vs.put(new PointImpl(v.getX(), v.getY()), v);
			}
		}
		return ret;
	}

	public Vertex getVertex(Point p) {
		return vs.get(p);
	}

	@Override
	public boolean removeSubGraph(Graph sub) {
		boolean ret = super.removeSubGraph(sub);
		if (ret) {
			Iterator<Vertex> i = sub.verticesIterator();
			Vertex v;
			while (i.hasNext()) {
				v = i.next();
				vs.remove(new PointImpl(v.getX(), v.getY()));
			}
		}
		return ret;
	}

	@Override
	public void setParent(Graph parent) {
		throw new UnsupportedOperationException();
	}

}
