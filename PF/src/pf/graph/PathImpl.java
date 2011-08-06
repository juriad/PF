package pf.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link Path}.
 * <p>
 * It stores separately both edges and vertices, because some paths over
 * multiple edges are hard to calculate lastVertex.
 * 
 * @author Adam Juraszek
 * 
 */
public class PathImpl implements Path {

	List<Edge> edges;
	List<Vertex> vertices;

	public PathImpl(Path p) {
		this(p.getFirstVertex());
		this.extend(p);
	}

	public PathImpl(Vertex start) {
		if (start == null) {
			throw new IllegalArgumentException();
		}
		edges = new ArrayList<Edge>();
		vertices = new ArrayList<Vertex>();
		vertices.add(start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PathImpl other = (PathImpl) obj;
		if (edges == null) {
			if (other.edges != null) {
				return false;
			}
		} else if (!edges.equals(other.edges)) {
			return false;
		}
		return true;
	}

	@Override
	public void extend(Edge e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (getLastVertex().equals(e.getV1())
				|| getLastVertex().equals(e.getV2())) {
			edges.add(e);
			vertices.add(e.getOther(getLastVertex()));
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void extend(Path p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		if (!getLastVertex().equals(p.getFirstVertex())) {
			throw new IllegalArgumentException();
		}
		for (Edge e : p) {
			extend(e);
		}
	}

	@Override
	public Edge getFirst() {
		if (length() > 0) {
			return edges.get(0);
		}
		return null;
	}

	@Override
	public Vertex getFirstVertex() {
		return vertices.get(0);
	}

	@Override
	public Edge getLast() {
		if (length() > 0) {
			return edges.get(length() - 1);
		}
		return null;
	}

	@Override
	public Vertex getLastVertex() {
		return vertices.get(vertices.size() - 1);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + edges.hashCode();
		return result;
	}

	@Override
	public Iterator<Edge> iterator() {
		return new Iterator<Edge>() {

			Iterator<Edge> i = edges.iterator();

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public Edge next() {
				return i.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int length() {
		return edges.size();
	}

	@Override
	public Edge shorten() {
		if (length() <= 0) {
			throw new IllegalArgumentException();
		}
		int index = length() - 1;
		vertices.remove(index + 1);
		return edges.remove(index);
	}

	@Override
	public Iterator<Vertex> verticesIterator() {
		return new Iterator<Vertex>() {

			Iterator<Vertex> i = vertices.iterator();

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public Vertex next() {
				return i.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
