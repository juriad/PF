package pf.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of {@link Graph}
 * <p>
 * Nothing special here.
 * 
 * @author Adam Juraszek
 * 
 */
public class GraphImpl implements Graph {

	private class EdgesIterator implements Iterator<Edge> {
		private Iterator<Graph> i;
		private Iterator<Edge> ie = null;
		private Edge next = null;
		private final Graph root;
		private final boolean used;

		public EdgesIterator(Graph root, boolean used) {
			i = subs.iterator();
			this.root = root;
			this.used = used;
			if (i.hasNext()) {
				ie = i.next().edgesIterator(root, used);
				next = getNext();
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Edge next() {
			Edge ret = next;
			next = getNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		private Edge getNext() {
			if (ie == null) {
				return null;
			}
			if (ie.hasNext()) {
				return ie.next();
			} else if (i.hasNext()) {
				ie = i.next().edgesIterator(root, used);
				return getNext();
			} else {
				return null;
			}
		}

	}

	private class VerticesIterator implements Iterator<Vertex> {
		private Iterator<Graph> i;
		private Iterator<Vertex> iv = null;
		private Vertex next = null;

		public VerticesIterator() {
			i = subs.iterator();
			if (i.hasNext()) {
				iv = i.next().verticesIterator();
				next = getNext();
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Vertex next() {
			Vertex ret = next;
			next = getNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		private Vertex getNext() {
			if (iv == null) {
				return null;
			}
			if (iv.hasNext()) {
				return iv.next();
			} else if (i.hasNext()) {
				iv = i.next().verticesIterator();
				return getNext();
			} else {
				return null;
			}
		}
	}

	private Graph parent;

	private Set<Graph> subs;

	/**
	 * the only constructor
	 * <p>
	 * if {@code parent == null} the graph is considered to be root.
	 * 
	 * @param parent
	 *            parent of this graph
	 */
	public GraphImpl(Graph parent) {
		this.parent = parent;
		subs = new HashSet<Graph>();
	}

	@Override
	public boolean addSubGraph(Graph sub) {
		return subs.add(sub);
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
	public Graph getParent() {
		return parent;
	}

	/**
	 * returns unmodifiable set of subsets
	 */
	@Override
	public Set<Graph> getSubGraphs() {
		return Collections.unmodifiableSet(subs);
	}

	@Override
	public boolean removeSubGraph(Graph sub) {
		return subs.remove(sub);
	}

	@Override
	public void setParent(Graph parent) {
		if (this.parent != parent) {
			if (this.parent != null) {
				this.parent.removeSubGraph(this);
			}
			if (parent != null) {
				parent.addSubGraph(this);
			}
			this.parent = parent;
		}
	}

	@Override
	public Iterator<Graph> subGraphsIterator() {
		return subs.iterator();
	}

	@Override
	public Iterator<Vertex> verticesIterator() {
		return new VerticesIterator();
	}

}
