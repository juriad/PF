package pf.graph;

public class DirectionImpl implements Direction {
	private final int dx;
	private final int dy;
	private final Direction opposite;
	private final boolean primary;

	public DirectionImpl(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		primary = true;
		opposite = new DirectionImpl(-dx, -dy, this);
	}

	protected DirectionImpl(int dx, int dy, Direction opposite) {
		this.dx = dx;
		this.dy = dy;
		primary = false;
		this.opposite = opposite;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Direction)) {
			return false;
		}
		Direction other = (Direction) obj;
		if (getDy() * other.getDx() == other.getDy() * getDx()) {
			if (other.getDy() / (double) dy > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getDx() {
		return dx;
	}

	@Override
	public int getDy() {
		return dy;
	}

	@Override
	public Direction getOpposite() {
		return opposite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (100 * dy / (float) dx);
		return result;
	}

	@Override
	public boolean isPrimary() {
		return primary;
	}
}
