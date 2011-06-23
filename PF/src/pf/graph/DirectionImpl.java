package pf.graph;

import pf.analytics.Vector;
import pf.analytics.VectorImpl;

public class DirectionImpl implements Direction {
	private final Vector d;
	private final Direction opposite;
	private final boolean primary;

	public DirectionImpl(int dx, int dy) {
		this(new VectorImpl(dx, dy));
	}

	public DirectionImpl(Vector d) {
		this.d = d;
		primary = true;
		opposite = new DirectionImpl(d.opposite(), this);
	}

	protected DirectionImpl(Vector d, Direction opposite) {
		this.d = d;
		primary = false;
		this.opposite = opposite;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DirectionImpl))
			return false;
		DirectionImpl other = (DirectionImpl) obj;
		return other.getVector().isLinearDependent(getVector());
	}

	@Override
	public int getDx() {
		return getVector().getX();
	}

	@Override
	public int getDy() {
		return getVector().getY();
	}

	@Override
	public Direction getOpposite() {
		return opposite;
	}

	@Override
	public Vector getVector() {
		return d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getDx();
		result = prime * result + getDy();
		return result;
	}

	@Override
	public boolean isPrimary() {
		return primary;
	}
}
