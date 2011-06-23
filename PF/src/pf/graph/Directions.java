package pf.graph;

import java.util.Set;

public interface Directions extends Iterable<Direction> {
	Set<Direction> getDirections();

	Direction getNearestDirection(int dx, int dy);
}
