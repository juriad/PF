package pf.graph;

public interface Edge {
	Vertex getCommon(Edge e2);

	Direction getDirection(Vertex from);

	Vertex getOther(Vertex v);

	Vertex getV1();

	Vertex getV2();

	boolean isUsed();

	void setUsed(boolean used);
}
