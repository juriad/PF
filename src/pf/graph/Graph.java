package pf.graph;

import java.util.Iterator;
import java.util.Set;

/**
 * This represents a general graph. It contains subgraphs and vertices. All this
 * is organized in a tree-like structure with vertices as leaves. Subgraphs are
 * usually components of graph.
 * <p>
 * There are no methods for manipulating graph here. This works mainly as an
 * entry point to graph. It has iterators over vertices and edges.
 * <p>
 * Parameter used mostly means a restriction to unused edges only.
 * 
 * @author Adam Juraszek
 * 
 */
public interface Graph {

	/**
	 * Adds a new subgraph to this graph
	 * 
	 * @param sub
	 *            subgraph
	 * @return {@link Set#add(Object)}
	 */
	boolean addSubGraph(Graph sub);

	/**
	 * Simplifies usage of {@link #edgesIterator(Graph, boolean)}.
	 * 
	 * @param used
	 *            shall restrict only on unused edges
	 * @return edges iterator
	 * @see #edgesIterator(Graph, boolean)
	 */
	Iterator<Edge> edgesIterator(boolean used);

	/**
	 * Auxiliary method which helps {@link #edgesIterator(boolean)}
	 * 
	 * Returns iterator over all edges inside of this graph and all primary
	 * edges from this graph to root.
	 * 
	 * @param root
	 *            component in which edges are
	 * @param used
	 *            shall restrict only on unused edges
	 * @return iterator over edges in component
	 * @see #edgesIterator(boolean)
	 */
	Iterator<Edge> edgesIterator(Graph root, boolean used);

	/**
	 * Gets parent graph of this graph. If the parent is null, this graph is
	 * root.
	 * 
	 * @return parent graph
	 */
	Graph getParent();

	/**
	 * Returns all subgraphs of this graph
	 * 
	 * @return set of subgraphs
	 * @see #subGraphsIterator()
	 */
	Set<Graph> getSubGraphs();

	/**
	 * Removes subgraph of this graph.
	 * 
	 * @param sub
	 *            subgraph of this
	 * @return see {@link Set#remove(Object)}
	 */
	boolean removeSubGraph(Graph sub);

	/**
	 * Sets a new parent for this graph. Removes itself from parent's subgraphs
	 * and adds itself into new parent's subgraphs.
	 * 
	 * @param parent
	 *            parent graph of this
	 */
	void setParent(Graph parent);

	/**
	 * Returns iterator over subgraphs of this graph. This doesn't iterate
	 * recursively.
	 * 
	 * @return iterator over subgraphs
	 * @see #getSubGraphs()
	 */
	Iterator<Graph> subGraphsIterator();

	/**
	 * Returns iterator over all vertices in this graph and all subgraphs. Each
	 * vertex has only one parent (Vertex{@link #getParent()}), so each vertex
	 * is returned only once.
	 * 
	 * @return iterator over vertices
	 */
	Iterator<Vertex> verticesIterator();
}
