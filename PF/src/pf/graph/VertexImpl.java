package pf.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VertexImpl implements Vertex {

	private class EdgesIterator implements Iterator<Edge> {
		private Iterator<Edge> i;
		private Edge next = null;
		private final Graph root;

		public EdgesIterator(Graph root, boolean used) {
			i = (used ? unusedEdges : edges).values().iterator();
			this.root = root;
			getNext();
		}

		private void getNext() {
			next = null;
			while (i.hasNext() && next == null) {
				Edge e = i.next();
				if (!isChild(e.getOther(VertexImpl.this))
						|| e.getDirection(VertexImpl.this).isPrimary())
					next = e;
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		private boolean isChild(Vertex v) {
			Graph g = v;
			while (g != null) {
				if (g.equals(root))
					return true;
				g = g.getParent();
			}
			return false;
		}

		@Override
		public Edge next() {
			Edge ret = next;
			getNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private Map<Direction, Edge> edges;

	private final Set<Graph> graphSet;
	private Graph parent;
	private Map<Direction, Edge> unusedEdges;
	private final Set<Vertex> vertexSet;
	private final int x;
	private final int y;

	public VertexImpl(Graph parent, int x, int y) {
		this.parent = parent;
		graphSet = new HashSet<Graph>();
		graphSet.add(this);
		vertexSet = new HashSet<Vertex>();
		vertexSet.add(this);
		edges = new HashMap<Direction, Edge>();
		unusedEdges = new HashMap<Direction, Edge>();
		this.x = x;
		this.y = y;
	}

	@Override
	public void add(Edge e) {
		Direction d = e.getDirection(this);
		edges.put(d, e);
		if (!e.isUsed())
			unusedEdges.put(d, e);
	}

	@Override
	public boolean addSubGraph(Graph sub) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Edge> edgesIterator(boolean used) {
		return edgesIterator(this, used);
	}

	@Override
	public Iterator<Edge> edgesIterator(Graph root, boolean used) {
		return new EdgesIterator(root, used);
	}

	@Override
	public void edgeUsageChanged(Edge e) {
		Direction d = e.getDirection(this);
		if (e.isUsed() && unusedEdges.containsKey(d))
			unusedEdges.remove(d);
		else if (!e.isUsed() && !unusedEdges.containsKey(d))
			unusedEdges.put(d, e);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VertexImpl other = (VertexImpl) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public Edge get(Direction d) {
		return edges.get(d);
	}

	@Override
	public int getDegree(boolean used) {
		if (used)
			return unusedEdges.size();
		return edges.size();
	}

	@Override
	public Set<Direction> getDirections(boolean used) {
		if (used)
			return unusedEdges.keySet();
		return edges.keySet();
	}

	@Override
	public Graph getParent() {
		return parent;
	}

	@Override
	public Set<Graph> getSubGraphs() {
		return graphSet;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public Iterator<Edge> iterator() {
		return iterator(false);
	}

	@Override
	public Iterator<Edge> iterator(boolean used) {
		return edgesIterator(used);
	}

	@Override
	public void remove(Edge e) {
		Direction d = e.getDirection(this);
		edges.remove(d);
		if (!e.isUsed())
			unusedEdges.remove(d);
	}

	@Override
	public boolean removeSubGraph(Graph sub) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setParent(Graph parent) {
		if (this.parent != parent) {
			if (this.parent != null)
				this.parent.removeSubGraph(this);
			if (parent != null)
				parent.addSubGraph(this);
			this.parent = parent;
		}
	}

	@Override
	public Iterator<Graph> subGraphsIterator() {
		return graphSet.iterator();
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

	@Override
	public Iterator<Vertex> verticesIterator() {
		return vertexSet.iterator();
	}

}
