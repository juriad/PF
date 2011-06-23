package pf.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DirectionsImpl implements Directions {

	private final Set<Direction> ds;

	public DirectionsImpl() {
		ds = new HashSet<Direction>();
	}

	public boolean addDirection(Direction d) {
		return ds.add(d);
	}

	@Override
	public Set<Direction> getDirections() {
		return Collections.unmodifiableSet(ds);
	}

	@Override
	public Direction getNearestDirection(int dx, int dy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Direction> iterator() {
		return ds.iterator();
	}

}
