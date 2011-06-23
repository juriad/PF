package pf.analytics;

public interface Vector {
	int getX();

	int getY();

	boolean isLinearDependent(Vector v);

	int lengthSq();

	Vector normal();

	Vector opposite();

	Vector scale(float s);
}
