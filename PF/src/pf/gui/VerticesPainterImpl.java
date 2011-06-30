package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pf.board.GridType;
import pf.graph.Vertex;

public class VerticesPainterImpl implements VerticesPainter {

	public enum DegreeType {
		BY_ALL {
			@Override
			public int degree(Vertex v) {
				return v.getDegree(false);
			}
		},
		BY_USED {
			@Override
			public int degree(Vertex v) {
				return v.getDegree(false) - v.getDegree(true);
			}
		},
		BY_UNUSED {
			@Override
			public int degree(Vertex v) {
				return v.getDegree(true);
			}
		};

		public abstract int degree(Vertex v);
	}

	protected class Info {

		private final int degree;
		Color color = Color.GREEN;
		int outerR = 5;
		int innerR = 3;

		public Info(int degree) {
			this.degree = degree;
		}

		public int getDegree() {
			return degree;
		}
	}

	private final GridType gt;
	private final DegreeType degreeType;
	protected List<Info> infos;

	public VerticesPainterImpl(GridType gt, DegreeType degreeType) {
		this.gt = gt;
		this.degreeType = degreeType;
		infos = new ArrayList<VerticesPainterImpl.Info>();
		for (int i = 0; i < gt.getLines() * 2 + 1; i++) {
			infos.add(new Info(i));
		}
	}

	public void drawVertex(Graphics2D g2d, GameBoard board, Vertex v) {
		int degree = degreeType.degree(v);
		g2d.setStroke(new BasicStroke(getOuterRadius(degree)
				- getInnerRadius(degree)));
		g2d.setColor(getColor(degree));
		int cx = board.translateXToScreen(v.getX());
		int cy = board.translateYToScreen(v.getY());
		g2d.drawOval(cx - getRadius(degree) / 2, cy - getRadius(degree) / 2,
				getRadius(degree), getRadius(degree));
	}

	@Override
	public Rectangle getBounds(GameBoard board, Vertex v) {
		int r = getRadius(v);
		int x = board.translateXToScreen(v.getX()) - 2 * r;
		int y = board.translateYToScreen(v.getY()) - 2 * r;
		return new Rectangle(x, y, 4 * r, 4 * r);
	}

	public Color getColor(int degree) {
		return infos.get(degree).color;
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public GridType getGridType() {
		return gt;
	}

	public int getInnerRadius(int degree) {
		return infos.get(degree).innerR;
	}

	public int getOuterRadius(int degree) {
		return infos.get(degree).outerR;
	}

	public int getRadius(int degree) {
		return getOuterRadius(degree);
	}

	public int getRadius(Vertex v) {
		return getRadius(getDegreeType().degree(v));
	}

	@Override
	public void paintVertices(Graphics2D g2d, GameBoard board) {
		Iterator<Vertex> vi = board.getBoard().getGraph().verticesIterator();
		Vertex v;
		while (vi.hasNext()) {
			v = vi.next();

			int r = getRadius(v);
			int x = board.translateXToScreen(v.getX() - 2 * r);
			int y = board.translateYToScreen(v.getY() - 2 * r);

			Rectangle rr = new Rectangle(x, y, 4 * r, 4 * r);

			if (g2d.getClipBounds().intersects(rr)) {
				drawVertex(g2d, board, v);
			}
		}
	}

	public void setColor(int degree, Color color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		infos.get(degree).color = color;
	}

	public void setColors(Color color) {
		for (int i = 0; i < infos.size(); i++) {
			setColor(i, color);
		}
	}

	public void setRadius(int degree, int radius) {
		if (radius <= 0) {
			throw new IllegalArgumentException();
		}
		infos.get(degree).innerR = 0;
		infos.get(degree).outerR = radius;
	}

	public void setRadius(int degree, int outer, int inner) {
		if (inner < 0 || outer <= inner) {
			throw new IllegalArgumentException();
		}
		infos.get(degree).innerR = inner;
		infos.get(degree).outerR = outer;
	}

	public void setRadiuses(int radius) {
		for (int i = 0; i < infos.size(); i++) {
			setRadius(i, radius);
		}
	}

	public void setRadiuses(int outer, int inner) {
		for (int i = 0; i < infos.size(); i++) {
			setRadius(i, outer, inner);
		}
	}
}
