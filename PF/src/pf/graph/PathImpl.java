package pf.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	Map<Edge, Integer> pointers;

	public PathImpl() {
		edges = new ArrayList<Edge>();
		pointers = new HashMap<Edge, Integer>();
	}

	@Override
	public void extend(Edge e) {
		if (e == null) {
			throw new IllegalArgumentException();
		}
		if (length() == 0) {
			pointers.put(e, 0);
			edges.add(e);
		} else if (length() == 1) {
			if (getFirst().getCommon(e) != null) {
				pointers.put(e, 1);
				edges.add(e);
			} else {
				throw new IllegalArgumentException();
			}
		} else if (getLastVertex().equals(e.getV1())
				|| getLastVertex().equals(e.getV2())) {
			pointers.put(e, length());
			edges.add(e);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void extend(Path p) {
		if (length() == 0) {
			int index = 0;
			for (Edge e : p) {
				edges.add(e);
				pointers.put(e, index);
				index++;
			}
		} else if (p.length() == 0) {
			;
		} else if (p.length() == 1) {
			extend(p.getFirst());
		} else if (length() == 1) {
			if (getFirst().getCommon(p.getFirst()) != null) {
				int index = 1;
				for (Edge e : p) {
					edges.add(e);
					pointers.put(e, index);
					index++;
				}
			} else {
				throw new IllegalArgumentException();
			}
		} else if (getLastVertex().equals(p.getFirstVertex())) {
			int index = length();
			for (Edge e : p) {
				edges.add(e);
				pointers.put(e, index);
				index++;
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
	public void insert(Edge edge, Path p) {
		if (edge == null) {
			throw new IllegalArgumentException();
		}
		if (getLast().equals(edge)) {
			extend(p);
			return;
		} else if (p.length() == 0) {
			return;
		}

		int index = pointers.get(edge);
		Vertex v = edges.get(index + 1).getCommon(edge);
		if (!p.getFirstVertex().equals(v) || !p.getLastVertex().equals(v)) {
			throw new IllegalArgumentException();
		}
		for (Edge e : p) {
			edges.add(index++, e);
		}

		pointers = new HashMap<Edge, Integer>();
		index = 0;
		for (Edge e : this) {
			pointers.put(e, index);
			index++;
		}
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
	public void shorten() {
		if (length() <= 0) {
			throw new IllegalArgumentException();
		}
		int index = pointers.get(length() - 1);
		pointers.remove(getLast());
		edges.remove(index);
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
