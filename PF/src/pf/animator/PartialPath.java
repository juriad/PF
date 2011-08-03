package pf.animator;

import java.util.Iterator;

import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.Vertex;

public class PartialPath implements Path {

	private final Path full;
	private int length;

	public PartialPath(Path full) {
		if (full == null) {
			throw new IllegalArgumentException();
		}
		this.full = full;
	}

	@Override
	public void extend(Edge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void extend(Path path) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge getFirst() {
		return full.getFirst();
	}

	@Override
	public Vertex getFirstVertex() {
		return full.getFirstVertex();
	}

	@Override
	public Edge getLast() {
		Edge ee = null;
		Iterator<Edge> ie = iterator();
		while (ie.hasNext()) {
			ee = ie.next();
		}
		return ee;
	}

	@Override
	public Vertex getLastVertex() {
		Vertex vv = null;
		Iterator<Vertex> iv = verticesIterator();
		while (iv.hasNext()) {
			vv = iv.next();
		}
		return vv;
	}

	@Override
	public Iterator<Edge> iterator() {
		final int len = length;
		return new Iterator<Edge>() {

			int index = 0;
			Iterator<Edge> ie = full.iterator();

			@Override
			public boolean hasNext() {
				return ie.hasNext() && index < len - 1;
			}

			@Override
			public Edge next() {
				index++;
				return ie.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int length() {
		return length;
	}

	public int realLength() {
		return full.length();
	}

	public void setLength(int length) {
		if (length < 0 || length > realLength()) {
			throw new IllegalArgumentException();
		}
		this.length = length;
	}

	@Override
	public Edge shorten() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Vertex> verticesIterator() {
		final int len = length;
		return new Iterator<Vertex>() {

			int index = 0;
			Iterator<Vertex> iv = full.verticesIterator();

			@Override
			public boolean hasNext() {
				return iv.hasNext() && index < len;
			}

			@Override
			public Vertex next() {
				index++;
				return iv.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
