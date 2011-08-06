package pf.graph;

/**
 * Edge is a connection between two {@link Vertex}es in a graph. Edge is
 * represented by Vertices where it ends. Edge also has a used flag.
 * <p>
 * Because this package is designed to work on a board with grid, each edge has
 * a direction.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Edge {

	/**
	 * Gets common vertex of two edges
	 * 
	 * @param edge2
	 * @return common vertex for two edges
	 * @see #getV1()
	 * @see #getV2()
	 */
	Vertex getCommon(Edge edge2);

	/**
	 * Gets direction of edge with the respect to the start Vertex from
	 * 
	 * @param from
	 *            start vertex
	 * @return direction of edge
	 */
	Direction getDirection(Vertex from);

	/**
	 * Gets the other end vertex of this edge to vertex
	 * 
	 * @param vertex
	 * @return the other vertex
	 */
	Vertex getOther(Vertex vertex);

	/**
	 * Gets one of ends of this edge
	 * 
	 * @return end vertex of edge
	 * @see #getV2()
	 * @see #getOther(Vertex)
	 */
	Vertex getV1();

	/**
	 * Gets the other end of this edge
	 * 
	 * @return end vertex of edge
	 * @see #getV1()
	 * @see #getOther(Vertex)
	 */
	Vertex getV2();

	/**
	 * @return true if edge is used, false otherwise
	 */
	boolean isUsed();

	/**
	 * Sets new usage of this edge. Informs both vertices about the change
	 * 
	 * @param used
	 *            new usage
	 * @see Vertex#edgeUsageChanged(Edge)
	 */
	void setUsed(boolean used);
}
