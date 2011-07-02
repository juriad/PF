package pf.graph;

public interface Direction {
	int getDx();

	int getDy();

	Direction getOpposite();

	boolean isPrimary();
}
