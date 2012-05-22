package pf.graph;

import java.util.Iterator;
import java.util.Set;

/**
 * {@link Graph} provides a tree-like structure: it contains other {@link Graph}
 * s and leaves are {@link Vertex}es. This is the reason why Vertex <b>is</b> a
 * Graph.
 * <p>
 * Vertex provides all methods for edge management. Vertex has a location
 * specified by integer x and y. It doesn't use Point from analytics package and
 * it rather has its own implementation.
 * <p>
 * There often is a parameter used in some methods. It has a meaning of a
 * restriction on usage of edges.
 * <ul>
 * <li>true - only unused edges
 * <li>false - all edges
 * </ul>
 * 
 * @author Adam Juraszek
 * 
 */
public interface Vertex extends Graph, Iterable<Edge> {

	/**
	 * Adds edge to this vertex. Edge is put into a map. Key is its direction
	 * which is determined from the edge by {@link Edge#getDirection(Vertex)}.
	 * 
	 * @param edge
	 */
	void add(Edge edge);

	/**
	 * Asks for an edge from this to vertex. If such edge exists, it is
	 * returned, null otherwise.
	 * 
	 * @param vertex
	 *            the other vertex of the edge
	 * @return edge from this to vertex, null if doesn't exist
	 */
	Edge edgeToVertex(Vertex vertex);

	/**
	 * Updates local usage of edges cache. This should be called by
	 * {@link Edge#setUsed(boolean)}
	 * 
	 * @param edge
	 *            edge whose usage might have changed
	 */
	void edgeUsageChanged(Edge edge);

	/**
	 * Gets an edge from this vertex in specified direction.
	 * 
	 * @param direction
	 *            direction of edge
	 * @return edge in specified direction, null if doesn't exist
	 */
	Edge get(Direction direction);

	/**
	 * Gets a degree of this vertex, degree is a number of edges which end in
	 * this vertex. This provides a used restriction flag.
	 * 
	 * @param used
	 *            usage restriction
	 * @return count of edges ending in this vertex
	 * 
	 */
	int getDegree(boolean used);

	/**
	 * 
	 * Gets all directions of all edges e nding in this vertex. It also have a
	 * usage restriction flag.
	 * 
	 * @param used
	 *            usage restriction
	 * @return set of directions in which an edge exists.
	 */
	Set<Direction> getDirections(boolean used);

	/**
	 * Gets x part of location
	 * 
	 * @return x
	 */
	int getX();

	/**
	 * Gets y part of location
	 * 
	 * @return y
	 */
	int getY();

	/**
	 * Provides an iterator over all edges or over all unused edges ending in
	 * this vertex. Behavior is specified by a usage restriction flag.
	 * 
	 * @param used
	 *            usage restriction
	 * @return iterator over all edges ending in this vertex
	 */
	Iterator<Edge> iterator(boolean used);

	/**
	 * Removes an edge from this vertex.
	 * 
	 * @param edge
	 *            edge to remove
	 */
	void remove(Edge edge);
}