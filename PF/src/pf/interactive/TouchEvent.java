package pf.interactive;

import java.util.EventObject;

import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.Vertex;

public class TouchEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private final Path path;
	private final Vertex vertex;
	private final Edge edge;

	public TouchEvent(Object source, Path path, Vertex vertex, Edge edge) {
		super(source);
		this.path = path;
		this.vertex = vertex;
		this.edge = edge;
	}

	public Edge getEdge() {
		return edge;
	}

	public Path getPath() {
		return path;
	}

	public Vertex getVertex() {
		return vertex;
	}

}
