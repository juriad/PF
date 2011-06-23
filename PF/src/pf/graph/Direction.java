package pf.graph;

import pf.analytics.Vector;

public interface Direction {
	int getDx();

	int getDy();

	Direction getOpposite();

	Vector getVector();

	boolean isPrimary();
}
