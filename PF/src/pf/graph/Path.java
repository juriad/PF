package pf.graph;

import java.util.Iterator;

/**
 * Path stores a chain of edges. Each edge shares a vertex with the next one. It
 * can iterate over edges and vertices.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Path extends Iterable<Edge> {

	/**
	 * Makes this path longer, adds edge to the end.
	 * 
	 * @param edge
	 *            edge to concatenate
	 */
	void extend(Edge edge);

	/**
	 * Makes this path longer, adds all edges of path to the end
	 * 
	 * @param path
	 *            path to concatenate
	 */
	void extend(Path path);

	/**
	 * Gets the first edge of this path
	 * 
	 * @return first edge of this path
	 * @see #getLast()
	 */
	Edge getFirst();

	/**
	 * Gets the first vertex of this path. If path contains only one edge, any
	 * vertex is returned, but it should differ from that returned by
	 * {@link #getLastVertex()}.
	 * 
	 * @return first vertex of this path
	 * @see #getLastVertex()
	 */
	Vertex getFirstVertex();

	/**
	 * Gets the last edge of this path
	 * 
	 * @return last edge of this path
	 * @see #getFirst()
	 */
	Edge getLast();

	/**
	 * Gets the last vertex of this path. If path contains only one edge, any
	 * vertex is returned, but it should differ from that returned by
	 * {@link #getFirstVertex()}.
	 * 
	 * @return first vertex of this path
	 * @see #getFirstVertex()
	 */
	Vertex getLastVertex();

	/**
	 * Length is number of edges it contains.
	 * 
	 * @return length of this path
	 */
	int length();

	/**
	 * Removes the last edge of this path
	 * 
	 * @return
	 * 
	 */
	Edge shorten();

	/**
	 * Provides an iterator over vertices of this path. They are in the right
	 * order from the first to the last. If length is 1, the order is not clear.
	 * Vertices may repeat.
	 * 
	 * @return iterator over vertices.
	 */
	Iterator<Vertex> verticesIterator();
}
