package pf.reimpl;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Path2D<E extends Number & Comparable<E>> implements Shape {

	protected class Segment {
		private final SegmentType type;
		private final E[] coords;

		public Segment(SegmentType type, E... coords) {
			this.type = type;
			this.coords = coords;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Segment other = (Segment) obj;
			if (!this.getType().equals(other.getType())) {
				return false;
			}
			if (this.getCoords().length != other.getCoords().length) {
				return false;
			}
			for (int i = 0; i < coords.length; i++) {
				if (!this.getCoords()[i].equals(other.getCoords()[i])) {
					return false;
				}
			}
			return true;
		}

		public E[] getCoords() {
			return coords;
		}

		public SegmentType getType() {
			return type;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + type.getType();
			for (E e : coords) {
				result = prime * result + e.hashCode();
			}
			return result;
		}
	}

	protected enum SegmentType {
		SEG_MOVETO (0, 2),
		SEG_LINETO (1, 2),
		SEG_QUADTO (2, 4),
		SEG_CUBICTO (3, 6),
		SEG_CLOSE (4, 0);

		private final int type;
		private final int size;

		private SegmentType(int type, int size) {
			this.type = type;
			this.size = size;
		}

		public int getSize() {
			return size;
		}

		public int getType() {
			return type;
		}
	}

	class SelfIterator implements PathIterator {
		protected Path2D<E> path;
		protected int index;
		private final int end;

		public SelfIterator(Path2D<E> path, int start, int end) {
			end = end < 0 ? Integer.MAX_VALUE : end;
			if (start > end) {
				throw new IllegalStateException();
			}
			this.path = path;
			index = start;
			this.end = end;
		}

		@Override
		public int currentSegment(double[] coords) {
			Segment current = path.segments.get(index);
			for (int i = 0; i < current.getType().getSize(); i++) {
				coords[i] = current.coords[i].doubleValue();
			}
			return current.getType().getType();
		}

		@Override
		public int currentSegment(float[] coords) {
			Segment current = path.segments.get(index);
			for (int i = 0; i < current.getType().getSize(); i++) {
				coords[i] = current.coords[i].floatValue();
			}
			return current.getType().getType();
		}

		@Override
		public int getWindingRule() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isDone() {
			return index >= path.length() || index == end;
		}

		@Override
		public void next() {
			index++;
		}
	}

	class TxIterator implements PathIterator {
		private final AffineTransform affine;
		private final SelfIterator iterator;

		public TxIterator(Path2D<E> path, AffineTransform at, int start, int end) {
			affine = at;
			iterator = new SelfIterator(path, start, end);
		}

		@Override
		public int currentSegment(double[] coords) {
			double[] tmp = new double[coords.length];
			int type = iterator.currentSegment(tmp);
			affine.transform(tmp, 0, coords, 0, coords.length / 2);
			return type;
		}

		@Override
		public int currentSegment(float[] coords) {
			float[] tmp = new float[coords.length];
			int type = iterator.currentSegment(tmp);
			affine.transform(tmp, 0, coords, 0, coords.length / 2);
			return type;
		}

		@Override
		public int getWindingRule() {
			return iterator.getWindingRule();
		}

		@Override
		public boolean isDone() {
			return iterator.isDone();
		}

		@Override
		public void next() {
			iterator.next();
		}
	}

	protected final List<Segment> segments;

	protected static final int INIT_SIZE = 20;
	protected static final int EXPAND_MAX = 500;

	/**
	 * Constructs a new empty single precision {@code Path2D} object with a
	 * default winding rule of {@link #WIND_NON_ZERO}.
	 * 
	 * @since 1.6
	 */
	public Path2D() {
		this(INIT_SIZE);
	}

	/**
	 * Constructs a new empty single precision {@code Path2D} object with the
	 * specified winding rule and the specified initial capacity to store path
	 * segments. This number is an initial guess as to how many path segments
	 * will be added to the path, but the storage is expanded as needed to store
	 * whatever path segments are added.
	 * 
	 * @param initialCapacity
	 *            the estimate for the number of path segments in the path
	 * @since 1.6
	 */
	public Path2D(int initialCapacity) {
		segments = new ArrayList<Path2D<E>.Segment>(initialCapacity);
	}

	/**
	 * Constructs a new single precision {@code Path2D} object from an arbitrary
	 * {@link Shape} object. All of the initial geometry and the winding rule
	 * for this path are taken from the specified {@code Shape} object.
	 * 
	 * @param s
	 *            the specified {@code Shape} object
	 * @since 1.6
	 */
	public Path2D(Path2D<E> gp) {
		segments = new ArrayList<Path2D<E>.Segment>(gp.segments);
	}

	/**
	 * Creates a new object of the same class as this object.
	 * 
	 * @return a clone of this instance.
	 * @exception OutOfMemoryError
	 *                if there is not enough memory.
	 * @see java.lang.Cloneable
	 * @since 1.6
	 */
	@Override
	public Object clone() {
		return new Path2D<E>(this);
	}

	/**
	 * Closes the current subpath by drawing a straight line back to the
	 * coordinates of the last {@code moveTo}. If the path is already closed
	 * then this method has no effect.
	 * 
	 * @since 1.6
	 */
	@SuppressWarnings("unchecked")
	public synchronized void closePath() {
		if (length() == 0
				|| !segments.get(length() - 1).getType()
						.equals(SegmentType.SEG_CLOSE)) {
			segments.add(new Segment(SegmentType.SEG_CLOSE));
		}
	}

	@Override
	public boolean contains(double x, double y) {
		return false;
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return false;
	}

	@Override
	public boolean contains(Point2D p) {
		return false;
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return false;
	}

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3,y3)}, using the
	 * specified points {@code (x1,y1)} and {@code (x2,y2)} as B&eacute;zier
	 * control points. All coordinates are specified in float precision.
	 * 
	 * @param x1
	 *            the X coordinate of the first B&eacute;zier control point
	 * @param y1
	 *            the Y coordinate of the first B&eacute;zier control point
	 * @param x2
	 *            the X coordinate of the second B&eacute;zier control point
	 * @param y2
	 *            the Y coordinate of the second B&eacute;zier control point
	 * @param x3
	 *            the X coordinate of the final end point
	 * @param y3
	 *            the Y coordinate of the final end point
	 * @see Path2D#curveTo
	 * @since 1.6
	 */
	@SuppressWarnings("unchecked")
	public synchronized void curveTo(E x1, E y1, E x2, E y2, E x3, E y3) {
		segments.add(new Segment(SegmentType.SEG_CUBICTO, x1, y1, x2, y2, x3,
				y3));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.6
	 */
	@Override
	public Rectangle getBounds() {
		return getBounds2D().getBounds();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.6
	 */
	@Override
	public synchronized Rectangle2D getBounds2D() {
		boolean first = true;
		E x1 = null, y1 = null, x2 = null, y2 = null;
		for (Segment s : segments) {
			for (int i = 0; i < s.getType().getSize(); i += 2) {
				E x = s.getCoords()[i];
				E y = s.getCoords()[i + 1];
				if (first) {
					x1 = x2 = x;
					y1 = y2 = y;
					first = false;
				} else {
					if (x.compareTo(x1) < 0) {
						x1 = x;
					}
					if (y.compareTo(y1) < 0) {
						y1 = y;
					}
					if (x.compareTo(x2) > 0) {
						x2 = x;
					}
					if (y.compareTo(y2) > 0) {
						y2 = y;
					}
				}
			}
		}
		return getRectangle(x1, y1, x2, y2);
	}

	/**
	 * Returns the coordinates most recently added to the end of the path as a
	 * {@link Point2D} object.
	 * 
	 * @return a {@code Point2D} object containing the ending coordinates of the
	 *         path or {@code null} if there are no points in the path.
	 * @since 1.6
	 */
	public synchronized Point2D getCurrentPoint() {
		if (length() < 1) {
			return null;
		}
		int index = length() - 1;
		Segment s = segments.get(index);
		if (!s.getType().equals(SegmentType.SEG_CLOSE)) {
			return getPoint(index);
		}

		for (index--; index >= 0; index--) {
			s = segments.get(index);
			if (s.getType().equals(SegmentType.SEG_MOVETO)) {
				return getPoint(index);
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The iterator for this class is not multi-threaded safe, which means that
	 * the {@code Path2D} class does not guarantee that modifications to the
	 * geometry of this {@code Path2D} object do not affect any iterations of
	 * that geometry that are already in process.
	 * 
	 * @since 1.6
	 */
	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		if (at == null) {
			return new SelfIterator(this, 0, -1);
		}
		return new TxIterator(this, at, 0, -1);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new FlatteningPathIterator(getPathIterator(at), flatness);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return false;
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return false;
	}

	public int length() {
		return segments.size();
	}

	/**
	 * Adds a point to the path by drawing a straight line from the current
	 * coordinates to the new specified coordinates specified in float
	 * precision.
	 * 
	 * @param x
	 *            the specified X coordinate
	 * @param y
	 *            the specified Y coordinate
	 * @see Path2D#lineTo
	 * @since 1.6
	 */
	@SuppressWarnings("unchecked")
	public synchronized void lineTo(E x, E y) {
		segments.add(new Segment(SegmentType.SEG_LINETO, x, y));
	}

	/**
	 * Adds a point to the path by moving to the specified coordinates specified
	 * in float precision.
	 * <p>
	 * This method provides a single precision variant of the double precision
	 * {@code moveTo()} method on the base {@code Path2D} class.
	 * 
	 * @param x
	 *            the specified X coordinate
	 * @param y
	 *            the specified Y coordinate
	 * @see Path2D#moveTo
	 * @since 1.6
	 */
	@SuppressWarnings("unchecked")
	public synchronized void moveTo(E x, E y) {
		if (length() > 0
				&& segments.get(length() - 1).getType()
						.equals(SegmentType.SEG_MOVETO)) {
			segments.remove(length() - 1);
		}
		segments.add(new Segment(SegmentType.SEG_MOVETO, x, y));
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by drawing
	 * a Quadratic curve that intersects both the current coordinates and the
	 * specified coordinates {@code (x2,y2)}, using the specified point
	 * {@code (x1,y1)} as a quadratic parametric control point. All coordinates
	 * are specified in float precision.
	 * <p>
	 * This method provides a single precision variant of the double precision
	 * {@code quadTo()} method on the base {@code Path2D} class.
	 * 
	 * @param x1
	 *            the X coordinate of the quadratic control point
	 * @param y1
	 *            the Y coordinate of the quadratic control point
	 * @param x2
	 *            the X coordinate of the final end point
	 * @param y2
	 *            the Y coordinate of the final end point
	 * @see Path2D#quadTo
	 * @since 1.6
	 */
	@SuppressWarnings("unchecked")
	public synchronized void quadTo(E x1, E y1, E x2, E y2) {
		segments.add(new Segment(SegmentType.SEG_QUADTO, x1, y1, x2, y2));
	}

	/**
	 * Resets the path to empty. The append position is set back to the
	 * beginning of the path and all coordinates and point types are forgotten.
	 * 
	 * @since 1.6
	 */
	public synchronized void reset() {
		segments.clear();
	}

	/**
	 * Gets end point of segment
	 * 
	 * @param index
	 *            index of segment
	 * @return end point
	 */
	protected Point2D getPoint(int index) {
		Segment s = segments.get(index);
		int ss = s.getType().getSize();
		return new Point2D.Float(s.getCoords()[ss - 2].floatValue(),
				s.getCoords()[ss - 1].floatValue());
	}

	protected Rectangle2D getRectangle(E x1, E y1, E x2, E y2) {
		float x1f = x1 == null ? 0 : x1.floatValue();
		float y1f = y1 == null ? 0 : y1.floatValue();
		float x2f = x2 == null ? 0 : x2.floatValue();
		float y2f = y2 == null ? 0 : y2.floatValue();
		return new Rectangle2D.Float(x1f, y1f, x2f - x1f, y2f - y1f);
	}
}