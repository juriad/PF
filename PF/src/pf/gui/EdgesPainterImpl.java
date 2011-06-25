package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.util.Iterator;

import pf.graph.Edge;

public class EdgesPainterImpl implements EdgesPainter {

	private Color usedColor = Color.GRAY;
	private Stroke unusedStroke = new BasicStroke();
	private boolean drawUnused = true;
	private Color unusedColor = Color.BLACK;
	private Stroke usedStroke = new BasicStroke();
	private boolean drawUsed = true;

	public EdgesPainterImpl(Color unusedColor, Color usedColor) {
		this(unusedColor, new BasicStroke(), usedColor, new BasicStroke());
	}

	public EdgesPainterImpl(Color unusedColor, Stroke unusedStroke,
			Color usedColor, Stroke usedStroke) {
		this.setUnusedColor(unusedColor);
		this.setUnusedStroke(unusedStroke);
		this.setUsedColor(usedColor);
		this.setUsedStroke(usedStroke);
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

	public Color getUnusedColor() {
		return unusedColor;
	}

	public Stroke getUnusedStroke() {
		return unusedStroke;
	}

	public Color getUsedColor() {
		return usedColor;
	}

	public Stroke getUsedStroke() {
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
		Iterator<Edge> ei = gameBoard.getBoard().getGraph()
				.edgesIterator(false);
		Edge e;
		Line2D.Float l;
		while (ei.hasNext()) {
			e = ei.next();
			int x1 = gameBoard.translateXToScreen(e.getV1().getX());
			int y1 = gameBoard.translateYToScreen(e.getV1().getY());
			int x2 = gameBoard.translateXToScreen(e.getV1().getX());
			int y2 = gameBoard.translateYToScreen(e.getV2().getY());
			l = new Line2D.Float(x1, y1, x2, y2);
			if (l.intersects(g2d.getClipBounds()))
				drawLine(g2d, e, l);
		}
	}

	public void setDrawUnused(boolean drawUnused) {
		this.drawUnused = drawUnused;
	}

	public void setDrawUsed(boolean drawUsed) {
		this.drawUsed = drawUsed;
	}

	public void setUnusedColor(Color unusedColor) {
		if (unusedColor == null)
			throw new IllegalArgumentException();
		this.unusedColor = unusedColor;
	}

	public void setUnusedStroke(Stroke unusedStroke) {
		if (unusedStroke == null)
			throw new IllegalArgumentException();
		this.unusedStroke = unusedStroke;
	}

	public void setUsedColor(Color usedColor) {
		if (usedColor == null)
			throw new IllegalArgumentException();
		this.usedColor = usedColor;
	}

	public void setUsedStroke(Stroke usedStroke) {
		if (usedStroke == null)
			throw new IllegalArgumentException();
		this.usedStroke = usedStroke;
	}

}
