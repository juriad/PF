package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import java.util.Iterator;

import pf.graph.Path;
import pf.graph.Vertex;
import pf.reimpl.Path2D;

public class PathPainterImpl implements PathPainter {
	public static class PathPainterImplFactory implements PathPainterFactory {
		private static volatile PathPainterImplFactory instance = null;

		public static PathPainterImplFactory getInstance() {
			if (instance == null) {
				synchronized (PathPainterImplFactory.class) {
					if (instance == null) {
						instance = new PathPainterImplFactory();
					}
				}
			}
			return instance;
		}

		private PathPainterImplFactory() {
		}

		@Override
		public PathPainter newInstance(InteractiveBoard board, Path path) {
			return new PathPainterImpl(board, path);
		}
	}

	protected final InteractiveBoard board;
	private final Path path;

	private Color color = Color.BLACK;
	private BasicStroke stroke = new BasicStroke();
	private float radius = 0.25f;

	public PathPainterImpl(InteractiveBoard board, Path path) {
		this.board = board;
		this.path = path;
	}

	@Override
	public Path2D<?> createPath2D() {
		Iterator<Vertex> vi = path.verticesIterator();
		Path2D<Integer> gp = new Path2D<Integer>();
		Vertex vv = null;
		boolean corner = false;
		while (vi.hasNext()) {
			Vertex v = vi.next();
			System.out.println(v);

			if (corner) {
				gp.quadTo(board.translateXToScreen(vv.getX()),
						board.translateYToScreen(vv.getY()),
						board.translateXToScreen(getCornerX(v, vv)),
						board.translateYToScreen(getCornerY(v, vv)));
			}

			if (path.getFirstVertex().equals(v)) {
				gp.moveTo(board.translateXToScreen(v.getX()),
						board.translateYToScreen(v.getY()));
			} else if (path.getLastVertex().equals(v)) {
				gp.lineTo(board.translateXToScreen(v.getX()),
						board.translateYToScreen(v.getY()));
			} else {
				gp.lineTo(board.translateXToScreen(getCornerX(vv, v)),
						board.translateYToScreen(getCornerY(vv, v)));
				corner = true;
			}

			vv = v;
		}
		return gp;

	}

	public Color getColor() {
		return color;
	}

	public float getCornerRadius() {
		return radius;
	}

	@Override
	public PathPainterFactory getFactory() {
		return PathPainterImplFactory.getInstance();
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	@Override
	public void paintPath(Graphics2D g2d, Path2D<?> p2d) {
		g2d.setColor(getColor());
		g2d.setStroke(getStroke());
		g2d.draw(p2d);

		PathIterator pi = p2d.getPathIterator(null);
		float[] coords = new float[6];
		while (!pi.isDone()) {
			System.out.println(pi.currentSegment(coords) + "  "
					+ Arrays.toString(coords));
			pi.next();
		}

	}

	public void setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		this.color = color;
	}

	public void setCornerRadius(float radius) {
		if (radius < 0 || radius > 0.5f) {
			throw new IllegalArgumentException();
		}
		this.radius = radius;
	}

	public void setStroke(BasicStroke stroke) {
		if (stroke == null) {
			throw new IllegalArgumentException();
		}
		this.stroke = stroke;
	}

	private float getCornerX(Vertex far, Vertex corner) {
		return corner.getX() + (far.getX() - corner.getX()) * radius;
	}

	private float getCornerY(Vertex far, Vertex corner) {
		return corner.getY() + (far.getY() - corner.getY()) * radius;
	}

}
