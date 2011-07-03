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
	 * Inserts whole path in the middle of this path. Position where to insert
	 * is determined by edge.
	 * 
	 * @param edge
	 *            after this edge is path inserted
	 * @param path
	 *            path to insert
	 */
	void insert(Edge edge, Path path);

	/**
	 * Length is number of edges it contains.
	 * 
	 * @return length of this path
	 */
	int length();

	/**
	 * Determines which edge follows after specified edge.
	 * 
	 * @param edge
	 *            edge to which is next relative
	 * @return the edge which follows after edge
	 * @see #previous(Edge)
	 */
	Edge next(Edge edge);

	/**
	 * Determines which edge precedes before specified edge.
	 * 
	 * @param edge
	 *            edge to which is previous relative
	 * @return the edge which precedes before edge
	 * @see #next(Edge)
	 */
	Edge previous(Edge edge);

	/**
	 * Removes the last edge of this path
	 * 
	 */
	void shorten();

	/**
	 * Provides an iterator over vertices of this path. They are in the right
	 * order from the first to the last. If length is 1, the order is not clear.
	 * Vertices may repeat.
	 * 
	 * @return iterator over vertices.
	 */
	Iterator<Vertex> verticesIterator();
}
