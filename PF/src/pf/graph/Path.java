package pf.graph;

import java.util.Iterator;

public interface Path extends Iterable<Edge> {
	void extend(Edge e);

	void extend(Path p);

	Edge getFirst();

	Vertex getFirstVertex();

	Edge getLast();

	Vertex getLastVertex();

	void insert(Edge e, Path p);

	boolean isClosed();

	int length();

	Edge next(Edge e);

	Edge previous(Edge e);

	boolean setClosed(boolean closed);

	void shorten(Edge e);

	Iterator<Vertex> verticesIterator();
}
