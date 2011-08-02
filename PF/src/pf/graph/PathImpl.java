package pf.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link Path}.
 * <p>
 * It stores each edge in two containers to provide both quick iteration and
 * quick next/previous queries.
 * 
 * @author Adam Juraszek
 * 
 */
public class PathImpl implements Path {

	List<Edge> edges;

	public PathImpl() {
		edges = new ArrayList<Edge>();
	}

	public PathImpl(Path p) {
		this();
		this.extend(p);
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
		if (length() == 0) {
			edges.add(e);
		} else if (length() == 1) {
			if (getFirst().getCommon(e) != null) {
				edges.add(e);
			} else {
				throw new IllegalArgumentException();
			}
		} else if (getLastVertex().equals(e.getV1())
				|| getLastVertex().equals(e.getV2())) {
			edges.add(e);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void extend(Path p) {
		if (length() == 0) {
			for (Edge e : p) {
				edges.add(e);
			}
		} else if (p.length() == 0) {
			;
		} else if (p.length() == 1) {
			extend(p.getFirst());
		} else if (length() == 1) {
			if (getFirst().getCommon(p.getFirst()) != null) {
				for (Edge e : p) {
					edges.add(e);
				}
			} else {
				throw new IllegalArgumentException();
			}
		} else if (getLastVertex().equals(p.getFirstVertex())) {
			for (Edge e : p) {
				edges.add(e);
			}
		} else {
			throw new IllegalArgumentException();
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
		if (length() == 0) {
			return null;
		} else if (length() == 1) {
			return edges.get(0).getV1();
		} else {
			return edges.get(0).getOther(edges.get(0).getCommon(edges.get(1)));
		}
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
		if (length() == 0) {
			return null;
		} else if (length() == 1) {
			return edges.get(0).getV2();
		} else {
			return edges.get(length() - 1).getOther(
					edges.get(length() - 1).getCommon(edges.get(length() - 2)));
		}
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
		// System.out.println(edges.size());
		return edges.size();
	}

	@Override
	public Edge shorten() {
		if (length() <= 0) {
			throw new IllegalArgumentException();
		}
		int index = length() - 1;
		return edges.remove(index);
	}

	@Override
	public Iterator<Vertex> verticesIterator() {
		return new Iterator<Vertex>() {

			int index = 0;
			int length = length();

			@Override
			public boolean hasNext() {
				if (length == 0) {
					return false;
				}
				return index <= length;
			}

			@Override
			public Vertex next() {
				Vertex n;
				if (index == 0) {
					n = getFirstVertex();
				} else if (index == length) {
					n = getLastVertex();
				} else {
					n = edges.get(index - 1).getCommon(edges.get(index));
				}
				index++;
				return n;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
