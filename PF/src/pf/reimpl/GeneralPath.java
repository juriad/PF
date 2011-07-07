package pf.reimpl;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class GeneralPath<E extends Number & Comparable<E>> extends Path2D<E> {

	public GeneralPath() {
		super();
	}

	public Rectangle2D getBound(PathIterator pi) {
		boolean first = true;
		float[] coords = new float[6];
		int types[] = new int[] { 2, 2, 4, 6, 0 };
		float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		do {
			int type = pi.currentSegment(coords);
			for (int i = 0; i < types[type]; i++) {
				float x = coords[i];
				float y = coords[i + 1];
				if (first) {
					x1 = x2 = x;
					y1 = y2 = y;
					first = false;
				} else {
					if (x < x1) {
						x1 = x;
					}
					if (y < y1) {
						y1 = y;
					}
					if (x > x2) {
						x1 = x;
					}
					if (y > y2) {
						y1 = y;
					}
				}
			}
		} while (!pi.isDone());
		return new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1);

	}

	public int getFirstDiff(Path2D<E> path2d) {
		for (int i = 0; i < length(); i++) {
			if (!segments.get(i).equals(path2d.segments.get(i))) {
				return i;
			}
		}
		if (length() != path2d.length()) {
			return length();
		}
		return -1;
	}

	public PathIterator getPathIterator(AffineTransform at, int start, int end) {
		if (at == null) {
			return new SelfIterator(this, 0, -1);
		}
		return new TxIterator(this, at, 0, -1);
	}

	public void shorten(int newLength) {
		for (int i = length() - 1; i > newLength; i++) {
			segments.remove(i);
		}
	}
}
