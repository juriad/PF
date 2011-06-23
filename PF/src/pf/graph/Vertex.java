package pf.graph;

import java.util.Iterator;
import java.util.Set;

public interface Vertex extends Graph, Iterable<Edge> {
	void add(Edge e);

	void edgeUsageChanged(Edge e);

	Edge get(Direction d);

	int getDegree(boolean used);

	Set<Direction> getDirections(boolean used);

	int getX();

	int getY();

	Iterator<Edge> iterator(boolean used);

	void remove(Edge e);
}