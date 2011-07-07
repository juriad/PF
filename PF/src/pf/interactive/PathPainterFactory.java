package pf.interactive;

import pf.graph.Path;

public interface PathPainterFactory {
	public PathPainter newInstance(InteractiveBoard board, Path path);
}
