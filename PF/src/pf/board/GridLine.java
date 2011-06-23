package pf.board;

import pf.analytics.Line;
import pf.analytics.Point;

public interface GridLine extends Line {
	int distanceSq();

	Line getLine(int parallel);

	int getNearest(Point p);

	Line getNearestLine(Point p);

}
