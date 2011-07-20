package pf.interactive;

import pf.analytics.Point;
import pf.graph.Vertex;

public interface SnapPolicy {
	Vertex request(GameBoard board, float x, float y, Point last);
}
