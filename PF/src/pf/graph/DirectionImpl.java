package pf.graph;

/**
 * Implementation of {@link Direction}.
 * <p>
 * Nothing special here, exept the constructor {@link #DirectionImpl(int, int)}
 * 
 * @author Adam Juraszek
 * 
 */
public class DirectionImpl implements Direction {
	private final int dx;
	private final int dy;
	private final Direction opposite;
	private final boolean primary;

	/**
	 * Constructs a new Direction and also an opposite Direction as well. This
	 * makes {@link #getOpposite()} work well.
	 * 
	 * @param dx
	 * @param dy
	 * @see #DirectionImpl(int, int, Direction)
	 */
	public DirectionImpl(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		primary = true;
		opposite = new DirectionImpl(-dx, -dy, this);
	}

	/**
	 * This constructor is used only by the public one. This constructs the
	 * opposite direction, in analytics: this and opposite direction are
	 * opposite vectors.
	 * 
	 * @param dx
	 * @param dy
	 * @param opposite
	 *            opposite direction crested by public constructor
	 */
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
