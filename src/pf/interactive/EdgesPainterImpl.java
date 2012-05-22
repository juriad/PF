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

/**
 * Implementation of {@link EdgesPainter}. This class can distinguish style of
 * used and unused edge.
 * 
 * @author Adam Juraszek
 * 
 */
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

	/**
	 * @return color of unused edge
	 */
	public Color getUnusedColor() {
		return unusedColor;
	}

	/**
	 * @return stroke of unused edge
	 */
	public BasicStroke getUnusedStroke() {
		return unusedStroke;
	}

	/**
	 * @return color of used edge
	 */
	public Color getUsedColor() {
		return usedColor;
	}

	/**
	 * @return stroke of used edge
	 */
	public BasicStroke getUsedStroke() {
		return usedStroke;
	}

	/**
	 * @return whether unused edge should be painted
	 */
	public boolean isDrawUnused() {
		return drawUnused;
	}

	/**
	 * @return whether used edge should be painted
	 */
	public boolean isDrawUsed() {
		return drawUsed;
	}

	/**
	 * don't paint worm holes
	 */
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

	/**
	 * Sets whether unused edge should be painted.
	 * 
	 * @param drawUnused
	 */
	public void setDrawUnused(boolean drawUnused) {
		this.drawUnused = drawUnused;
	}

	/**
	 * Sets whether used edge should be painted.
	 * 
	 * @param drawUsed
	 */
	public void setDrawUsed(boolean drawUsed) {
		this.drawUsed = drawUsed;
	}

	/**
	 * Sets color of unused edge.
	 * 
	 * @param unusedColor
	 */
	public void setUnusedColor(Color unusedColor) {
		if (unusedColor == null) {
			throw new IllegalArgumentException();
		}
		this.unusedColor = unusedColor;
	}

	/**
	 * Sets stroke of unused edge.
	 * 
	 * @param unusedStroke
	 */
	public void setUnusedStroke(BasicStroke unusedStroke) {
		if (unusedStroke == null) {
			throw new IllegalArgumentException();
		}
		this.unusedStroke = unusedStroke;
	}

	/**
	 * Sets color of used edge.
	 * 
	 * @param usedColor
	 */
	public void setUsedColor(Color usedColor) {
		if (usedColor == null) {
			throw new IllegalArgumentException();
		}
		this.usedColor = usedColor;
	}

	/**
	 * Sets stroke of used edge.
	 * 
	 * @param usedStroke
	 */
	public void setUsedStroke(BasicStroke usedStroke) {
		if (usedStroke == null) {
			throw new IllegalArgumentException();
		}
		this.usedStroke = usedStroke;
	}

	/**
	 * Draws edge e.
	 * 
	 * @param g2d
	 * @param e
	 *            edge to draw
	 * @param l
	 *            already converted edge to line
	 */
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

	/**
	 * Converts edge to line in screen coordination system.
	 * 
	 * @param gameBoard
	 * @param e
	 *            edge to convert
	 * @return line on screen
	 */
	protected Line2D.Float getLine(GameBoard gameBoard, Edge e) {
		int x1 = gameBoard.translateXToScreen(e.getV1().getX());
		int y1 = gameBoard.translateYToScreen(e.getV1().getY());
		int x2 = gameBoard.translateXToScreen(e.getV2().getX());
		int y2 = gameBoard.translateYToScreen(e.getV2().getY());
		return new Line2D.Float(x1, y1, x2, y2);
	}

}
