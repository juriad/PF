package pf.interactive;

import java.util.EventObject;

import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.Vertex;

/**
 * This event informs about changes of touch in {@link InteractiveBoard}
 * 
 * @author Adam Juraszek
 * 
 */
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

	/**
	 * @return edge which the path has been extended or shorted by
	 */
	public Edge getEdge() {
		return edge;
	}

	/**
	 * @return path of this touch
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * @return last vertex visited by cursor which caused this event
	 */
	public Vertex getVertex() {
		return vertex;
	}

}
