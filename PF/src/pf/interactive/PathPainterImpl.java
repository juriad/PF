package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import java.util.Iterator;

import pf.graph.Path;
import pf.graph.Vertex;
import pf.reimpl.GeneralPath;

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
	protected float radius = 0.25f;
	protected Color color = Color.BLACK;
	protected BasicStroke stroke = new BasicStroke();
	private final Path path;

	private GeneralPath<?> localPath;

	public PathPainterImpl(InteractiveBoard board, Path path) {
		this.board = board;
		this.path = path;
		localPath = createGeneralPath(path);
	}

	@Override
	public Rectangle getBounds() {
		// TODO autogen method: getBounds
		return null;
	}

	public Color getColor() {
		return color;
	}

	public float getCornerRadius() {
		return radius;
	}

	@Override
	public Rectangle getDiffBounds() {
		// TODO autogen method: getDiffBounds
		return null;
	}

	@Override
	public PathPainterFactory getFactory() {
		return PathPainterImplFactory.getInstance();
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	@Override
	public void paintPath(Graphics2D g2d, InteractiveBoard interactiveBoard,
			Path p) {
		localPath = createGeneralPath(path);
		g2d.draw(localPath);
		PathIterator pi = localPath.getPathIterator(null);
		float[] coords = new float[6];
		while (!pi.isDone()) {
			// pi.next();
			System.out.println(pi.currentSegment(coords) + "  "
					+ Arrays.toString(coords));
			pi.next();
			// break;
		}

	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setCornerRadius(float radius) {
		this.radius = radius;
	}

	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	private GeneralPath<?> createGeneralPath(Path p) {
		Iterator<Vertex> vi = p.verticesIterator();
		GeneralPath<Integer> gp = new GeneralPath<Integer>();
		Vertex vv = null;
		boolean corner = false;
		while (vi.hasNext()) {
			Vertex v = vi.next();
			System.out.println(v);
			/*
			 * if (p.getFirstVertex().equals(v)) {
			 * gp.moveTo(board.translateXToScreen(v.getX()),
			 * board.translateYToScreen(v.getY())); } else if
			 * (p.getLastVertex().equals(v)) {
			 * gp.quadTo(board.translateXToScreen(vv.getX()),
			 * board.translateYToScreen(vv.getY()),
			 * board.translateXToScreen(getCornerX(v, vv)),
			 * board.translateYToScreen(getCornerY(v, vv)));
			 * gp.lineTo(board.translateXToScreen(v.getX()),
			 * board.translateYToScreen(v.getY())); } else {
			 * gp.lineTo(board.translateXToScreen(getCornerX(vv, v)),
			 * board.translateYToScreen(getCornerY(vv, v)));
			 * gp.quadTo(board.translateXToScreen(vv.getX()),
			 * board.translateYToScreen(vv.getY()),
			 * board.translateXToScreen(getCornerX(v, vv)),
			 * board.translateYToScreen(getCornerY(v, vv))); }
			 */
			/*
			 * if (corner) { gp.quadTo((float) vv.getX(), (float) vv.getY(),
			 * getCornerX(v, vv), getCornerY(v, vv)); }
			 * 
			 * if (p.getFirstVertex().equals(v)) { gp.moveTo((float) v.getX(),
			 * (float) v.getY()); } else if (p.getLastVertex().equals(v)) {
			 * gp.lineTo((float) v.getX(), (float) v.getY()); } else {
			 * gp.lineTo(getCornerX(vv, v), getCornerY(vv, v)); corner = true; }
			 */

			System.out.println(board.translateXToScreen(v.getX()) + " "
					+ board.translateYToScreen(v.getY()));

			if (corner) {
				gp.quadTo(board.translateXToScreen(vv.getX()),
						board.translateYToScreen(vv.getY()),
						board.translateXToScreen(getCornerX(v, vv)),
						board.translateYToScreen(getCornerY(v, vv)));
			}

			if (p.getFirstVertex().equals(v)) {
				gp.moveTo(board.translateXToScreen(v.getX()),
						board.translateYToScreen(v.getY()));
			} else if (p.getLastVertex().equals(v)) {
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

	private float getCornerX(Vertex far, Vertex corner) {
		return corner.getX() + (far.getX() - corner.getX()) * radius;
	}

	private float getCornerY(Vertex far, Vertex corner) {
		return corner.getY() + (far.getY() - corner.getY()) * radius;
	}

}
