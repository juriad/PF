package pf.graph;

import java.util.Iterator;
import java.util.Set;

public interface Graph {
	boolean addSubGraph(Graph sub);

	Iterator<Edge> edgesIterator(boolean used);

	Iterator<Edge> edgesIterator(Graph root, boolean used);

	Graph getParent();

	Set<Graph> getSubGraphs();

	boolean removeSubGraph(Graph sub);

	void setParent(Graph parent);

	Iterator<Vertex> verticesIterator();
}
