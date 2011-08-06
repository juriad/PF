package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.util.Iterator;

import pf.animator.RandomizedIterator;
import pf.graph.Edge;

public class EdgesPainterImpl implements EdgesPainter {

	private Color usedColor;
	private BasicStroke unusedStroke;
	private boolean drawUnused = true;
	private Color unusedColor;
	private BasicStroke usedStroke;
	private boolean drawUsed = true;

	public EdgesPainterImpl() {
		this(Color.BLACK, Color.RED);
	}

	public EdgesPainterImpl(Color unusedColor, BasicStroke unusedStroke,
			Color usedColor, BasicStroke usedStroke) {
		setUnusedColor(unusedColor);
		setUnusedStroke(unusedStroke);
		setUsedColor(usedColor);
		setUsedStroke(usedStroke);
	}

	public EdgesPainterImpl(Color unusedColor, Color usedColor) {
		this(unusedColor, new BasicStroke(), usedColor, new BasicStroke());
	}

	@Override
	public Rectangle getBounds(GameBoard gameBoard, Edge e) {
		Float l = getLine(gameBoard, e);
		Shape s1 = getUnusedStroke().createStrokedShape(l);
		Rectangle r1 = s1.getBounds();
		Shape s2 = getUnusedStroke().createStrokedShape(l);
		Rectangle r2 = s2.getBounds();
		return r1.union(r2);
	}

	public Color getUnusedColor() {
		return unusedColor;
	}

	public BasicStroke getUnusedStroke() {
		return unusedStroke;
	}

	public Color getUsedColor() {
		return usedColor;
	}

	public BasicStroke getUsedStroke() {
		return usedStroke;
	}

	public boolean isDrawUnused() {
		return drawUnused;
	}

	public boolean isDrawUsed() {
		return drawUsed;
	}

	@Override
	public void paintEdges(Graphics2D g2d, GameBoard gameBoard) {
		Iterator<Edge> ei = new RandomizedIterator<Edge>(gameBoard.getBoard()
				.getGraph().edgesIterator(false));
		Edge e;
		while (ei.hasNext()) {
			e = ei.next();
			if (getBounds(gameBoard, e).intersects(g2d.getClipBounds())) {
				if (e.getDirection(e.getV1()).getDx() != Integer.MAX_VALUE
						&& e.getDirection(e.getV2()).getDx() != -Integer.MAX_VALUE) {
					drawLine(g2d, e, getLine(gameBoard, e));
				}
			}
		}
	}

	public void setDrawUnused(boolean drawUnused) {
		this.drawUnused = drawUnused;
	}

	public void setDrawUsed(boolean drawUsed) {
		this.drawUsed = drawUsed;
	}

	public void setUnusedColor(Color unusedColor) {
		if (unusedColor == null) {
			throw new IllegalArgumentException();
		}
		this.unusedColor = unusedColor;
	}

	public void setUnusedStroke(BasicStroke unusedStroke) {
		if (unusedStroke == null) {
			throw new IllegalArgumentException();
		}
		this.unusedStroke = unusedStroke;
	}

	public void setUsedColor(Color usedColor) {
		if (usedColor == null) {
			throw new IllegalArgumentException();
		}
		this.usedColor = usedColor;
	}

	public void setUsedStroke(BasicStroke usedStroke) {
		if (usedStroke == null) {
			throw new IllegalArgumentException();
		}
		this.usedStroke = usedStroke;
	}

	protected void drawLine(Graphics2D g2d, Edge e, Float l) {
		if (e.isUsed() && isDrawUsed()) {
			g2d.setColor(usedColor);
			g2d.setStroke(usedStroke);
		} else if (!e.isUsed() && isDrawUnused()) {
			g2d.setColor(unusedColor);
			g2d.setStroke(unusedStroke);
		}
		g2d.draw(l);
	}

	protected Line2D.Float getLine(GameBoard gameBoard, Edge e) {
		int x1 = gameBoard.translateXToScreen(e.getV1().getX());
		int y1 = gameBoard.translateYToScreen(e.getV1().getY());
		int x2 = gameBoard.translateXToScreen(e.getV2().getX());
		int y2 = gameBoard.translateYToScreen(e.getV2().getY());
		return new Line2D.Float(x1, y1, x2, y2);
	}

}
