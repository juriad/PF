package pf.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphImpl implements Graph {

	private class EdgesIterator implements Iterator<Edge> {
		private Iterator<Graph> i;
		private Iterator<Edge> ie;
		private Edge next = null;
		private final Graph root;
		private final boolean used;

		public EdgesIterator(Graph root, boolean used) {
			i = subs.iterator();
			this.root = root;
			this.used = used;
			ie = i.next().edgesIterator(root, used);
			// FIXME graph has no children
			next = getNext();
		}

		private Edge getNext() {
			if (ie.hasNext())
				return ie.next();
			else if (i.hasNext()) {
				ie = i.next().edgesIterator(root, used);
				return getNext();
			} else
				return null;
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

	}

	private class VerticesIterator implements Iterator<Vertex> {
		private Iterator<Graph> i;
		private Iterator<Vertex> iv;
		private Vertex next = null;

		public VerticesIterator() {
			i = subs.iterator();
			if (i.hasNext()) {
				iv = i.next().verticesIterator();
				next = getNext();
			}
		}

		private Vertex getNext() {
			if (iv.hasNext())
				return iv.next();
			else if (i.hasNext()) {
				iv = i.next().verticesIterator();
				return getNext();
			} else
				return null;
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
	}

	private Graph parent;

	private Set<Graph> subs;

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
			if (this.parent != null)
				this.parent.removeSubGraph(this);
			if (parent != null)
				parent.addSubGraph(this);
			this.parent = parent;
		}
	}

	@Override
	public Iterator<Vertex> verticesIterator() {
		return new VerticesIterator();
	}

}