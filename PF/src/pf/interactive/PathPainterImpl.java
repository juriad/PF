package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import pf.graph.Path;
import pf.graph.Vertex;

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

	public Color getColor() {
		return color;
	}

	public float getCornerRadius() {
		return radius;
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	@Override
	public void paintPath(Graphics2D g2d, Path2D<?> p2d) {
		g2d.setColor(getColor());
		g2d.setStroke(getStroke());
		g2d.draw(p2d);
	}

	public void set(PathPainterImpl ppi) {
		setColor(ppi.getColor());
		setCornerRadius(ppi.getCornerRadius());
		setStroke(ppi.getStroke());
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

	public void setPath(Path path) {
		this.path = path;
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
