package pf.interactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathPainters {

	private final List<PathPainterFactory> pathPainters;

	private static volatile PathPainters instance = null;

	public static PathPainters getInstance() {
		if (instance == null) {
			synchronized (PathPainters.class) {
				if (instance == null) {
					instance = new PathPainters();
				}
			}
		}
		return instance;
	}

	private PathPainters() {
		pathPainters = new ArrayList<PathPainterFactory>();
	}

	public void addPathPainter(PathPainterFactory pathPainter) {
		pathPainters.add(pathPainter);
	}

	public List<PathPainterFactory> getPathPainters() {
		return Collections.unmodifiableList(pathPainters);
	}
}
