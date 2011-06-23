package pf.board;

import pf.analytics.Point;
import pf.board.BoardPattern.PointsEdge;

public interface BoardPattern extends Iterable<PointsEdge> {
	static class PointsEdge {
		final Point p1, p2;

		public PointsEdge(Point p1, Point p2) {
			if (p1 == null || p2 == null)
				throw new IllegalArgumentException();
			this.p1 = p1;
			this.p2 = p2;
		}
	}
}
