package pf.analytics;

public class VectorImpl implements Vector {

	private final int x;
	private final int y;

	public VectorImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VectorImpl other = (VectorImpl) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean isLinearDependent(Vector v) {
		return v.getX() * getY() == getX() * v.getY();
	}

	@Override
	public int lengthSq() {
		return x * x + y * y;
	}

	@Override
	public Vector normal() {
		return new VectorImpl(y, -x);
	}

	@Override
	public Vector opposite() {
		return scale(-1);
	}

	@Override
	public Vector scale(float s) {
		return new VectorImpl((int) (x * s), (int) (y * s));
	}

}