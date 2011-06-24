package pf.graph;

public class EdgeImpl implements Edge {
	private final Direction d;
	private boolean used;
	private final Vertex v1;
	private final Vertex v2;

	public EdgeImpl(Vertex v1, Vertex v2, Direction d) {
		if (v1 == null || v2 == null || d == null)
			throw new IllegalArgumentException();
		this.v1 = v1;
		this.v2 = v2;
		this.d = d;
		used = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return v1.equals(other.getV1()) && v2.equals(other.getV2())
				|| v2.equals(other.getV1()) && v1.equals(other.getV2());
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (v1.hashCode() + v2.hashCode());
		return result;
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

	@Override
	public String toString() {
		return "Edge [v1=" + v1 + ", v2=" + v2 + ", used=" + used + "]";
	}
}
