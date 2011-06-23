package pf.analytics;

public class VectorImpl implements Vector {

	private final int x;
	private final int y;

	public VectorImpl(int x, int y) {
		this.x = x;
		this.y = y;
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
