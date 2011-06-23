package pf.graph;

public class EdgeImpl implements Edge {
	private final Direction d;
	private boolean used;
	private final Vertex v1;
	private final Vertex v2;

	public EdgeImpl(Vertex v1, Vertex v2, Direction d) {
		this.v1 = v1;
		this.v2 = v2;
		this.d = d;
		used = false;
	}

	@Override
	public Vertex getCommon(Edge e2) {
		if (e2 == null)
			throw new NullPointerException();
		if (v1 == e2.getV1() || v1 == e2.getV2())
			return v1;
		else if (v2 == e2.getV1() || v2 == e2.getV2())
			return v2;
		else
			return null;
	}

	@Override
	public Direction getDirection(Vertex from) {
		if (from == v1)
			return d;
		else if (from == v2)
			return d.getOpposite();
		else
			throw new IllegalArgumentException();
	}

	@Override
	public Vertex getOther(Vertex v) {
		if (v1 == v)
			return v2;
		else if (v2 == v)
			return v1;
		else
			throw new IllegalArgumentException();
	}

	@Override
	public Vertex getV1() {
		return v1;
	}

	@Override
	public Vertex getV2() {
		return v2;
	}

	@Override
	public boolean isUsed() {
		return used;
	}

	@Override
	public void setUsed(boolean used) {
		if (this.used != used) {
			this.used = used;
			v1.edgeUsageChanged(this);
			v2.edgeUsageChanged(this);
		}
	}
}
