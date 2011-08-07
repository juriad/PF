package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import pf.graph.Path;
import pf.graph.Vertex;

/**
 * Implementation of {@link PathPainter}. This class allows to define custom
 * properties of painted path.
 * 
 * @author Adam Juraszek
 * 
 */
public class PathPainterImpl implements PathPainter {

	protected final InteractiveBoard board;
	private Path path;

	private Color color = Color.BLACK;
	private BasicStroke stroke = new BasicStroke();
	private float radius = 0.25f;

	public PathPainterImpl(InteractiveBoard board) {
		this.board = board;
		path = null;
	}

	/**
	 * Translates path to screen coordinates and rounds its corners.
	 * 
	 * @return path translated to screen coordinates
	 */
	@Override
	public Path2D<?> createPath2D() {
		if (path == null) {
			throw new IllegalStateException();
		}
		Iterator<Vertex> vi = path.verticesIterator();
		Path2D<Integer> gp = new Path2D<Integer>();
		Vertex vv = null;
		boolean corner = false;
		boolean first = true;

		Vertex v = null;
		while (vi.hasNext()) {
			v = vi.next();
			if (corner) {
				gp.quadTo(board.translateXToScreen(vv.getX()),
						board.translateYToScreen(vv.getY()),
						board.translateXToScreen(getCornerX(v, vv)),
						board.translateYToScreen(getCornerY(v, vv)));
			}

			if (first) {
				gp.moveTo(board.translateXToScreen(v.getX()),
						board.translateYToScreen(v.getY()));
				first = false;
			} else {

				gp.lineTo(board.translateXToScreen(getCornerX(vv, v)),
						board.translateYToScreen(getCornerY(vv, v)));
				corner = true;
			}
			vv = v;
		}
		gp.lineTo(board.translateXToScreen(v.getX()),
				board.translateYToScreen(v.getY()));
		return gp;

	}

	/**
	 * @return color of this path
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return radius of corners of this path
	 */
	public float getCornerRadius() {
		return radius;
	}

	/**
	 * @return stroke of this path
	 */
	public BasicStroke getStroke() {
		return stroke;
	}

	@Override
	public void paintPath(Graphics2D g2d, Path2D<?> p2d) {
		g2d.setColor(getColor());
		g2d.setStroke(getStroke());
		g2d.draw(p2d);
	}

	/**
	 * Copies all properties from another painter.
	 * 
	 * @param ppi
	 *            another painter
	 */
	public void set(PathPainterImpl ppi) {
		setColor(ppi.getColor());
		setCornerRadius(ppi.getCornerRadius());
		setStroke(ppi.getStroke());
	}

	/**
	 * Sets color of path
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		this.color = color;
	}

	/**
	 * Sets radius of corners of path
	 * 
	 * @param radius
	 */
	public void setCornerRadius(float radius) {
		if (radius < 0 || radius > 0.5f) {
			throw new IllegalArgumentException();
		}
		this.radius = radius;
	}

	/**
	 * Sets path this painter will paint
	 * 
	 * @param path
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * Sets stroke of path
	 * 
	 * @param stroke
	 */
	public void setStroke(BasicStroke stroke) {
		if (stroke == null) {
			throw new IllegalArgumentException();
		}
		this.stroke = stroke;
	}

	/**
	 * Calculates a corner segment from last vertex around corner to far vertex
	 * 
	 * @param far
	 * @param corner
	 * @return
	 */
	private float getCornerX(Vertex far, Vertex corner) {
		return corner.getX() + (far.getX() - corner.getX()) * radius;
	}

	/**
	 * Calculates a corner segment from last vertex around corner to far vertex
	 * 
	 * @param far
	 * @param corner
	 * @return
	 */
	private float getCornerY(Vertex far, Vertex corner) {
		return corner.getY() + (far.getY() - corner.getY()) * radius;
	}

}
