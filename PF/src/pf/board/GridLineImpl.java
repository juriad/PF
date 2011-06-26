package pf.board;

import pf.analytics.Line;
import pf.analytics.LineImpl;
import pf.analytics.Point;
import pf.analytics.Vector;

public class GridLineImpl extends LineImpl implements GridLine {
	private final Point p3;
	private final Vector v;

	public GridLineImpl(Point p1, Point p2, Point p3) {
		super(p1, p2);
		this.p3 = p3;
		this.v = p1.vectorTo(p3);
	}

	@Override
	public double distanceSq() {
		return distanceSq(p3);
	}

	@Override
	public Line getLine(int parallel) {
		return move(v.scale(parallel));
	}

	@Override
	public int getNearest(Point p) {
		double d = getLine(0).distanceSq(p);
		int i = (int) Math.round(Math.sqrt(d / distanceSq()));
		double d2 = getLine(i).distanceSq(p);
		return d >= d2 ? i : -i;
	}

	@Override
	public Line getNearestLine(Point p) {
		return getLine(getNearest(p));
	}

}
