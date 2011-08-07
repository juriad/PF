package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import pf.analytics.Line;
import pf.board.Grid;
import pf.board.GridLine;
import pf.board.GridType;

/**
 * Implementation of {@link GridPainter} which can paint each gridline its own
 * color and stroke. Moreover it can draw each n-th line of grid its own style.
 * 
 * @author Adam Juraszek
 * 
 */
public class GridPainterImpl implements GridPainter {

	/**
	 * Inner class containing data
	 * 
	 * @author Adam Juraszek
	 * 
	 */
	protected class Info {
		final int line;
		Color color = Color.ORANGE;
		Color mainColor = Color.YELLOW;
		Stroke stroke = new BasicStroke();
		Stroke mainStroke = new BasicStroke(2);
		int offset = 0;
		int repetition = 3;

		public Info(int line) {
			this.line = line;
		}
	}

	private final GridType gt;

	protected List<Info> infos;

	/**
	 * Grid painter needs to know how many gridlines exists in this grid
	 * 
	 * @param gt
	 */
	public GridPainterImpl(GridType gt) {
		this.gt = gt;
		infos = new ArrayList<Info>();
		for (int i = 0; i < getLines(); i++) {
			infos.add(new Info(i));
		}
	}

	/**
	 * @param line
	 * @return color of ordinary line of gridline line
	 */
	public Color getColor(int line) {
		return infos.get(line).color;
	}

	/**
	 * @return type of grid which this grid painter can paint
	 */
	public GridType getGridType() {
		return gt;
	}

	/**
	 * @return number of gridlines in grid
	 */
	public int getLines() {
		return gt.getLines();
	}

	/**
	 * @param line
	 * @return color of main line of gridline line
	 */
	public Color getMainColor(int line) {
		return infos.get(line).mainColor;
	}

	/**
	 * @param line
	 * @return stroke of main line of gridline line
	 */
	public Stroke getMainStroke(int line) {
		return infos.get(line).mainStroke;
	}

	/**
	 * @param line
	 * @return main line start offset of gridline line
	 */
	public int getOffset(int line) {
		return infos.get(line).offset;
	}

	/**
	 * @param line
	 * @return main line repetition of gridline line
	 */
	public int getRepetition(int line) {
		return infos.get(line).repetition;
	}

	/**
	 * @param line
	 * @return stroke of ordinary line of gridline line
	 */
	public Stroke getStroke(int line) {
		return infos.get(line).stroke;
	}

	@Override
	public void paintGrid(Graphics2D g2d, GameBoard board) {
		if (board.getBoard().getGrid().getGridType() != getGridType()) {
			throw new IllegalArgumentException();
		}
		Grid grid = board.getBoard().getGrid();

		Rectangle r = g2d.getClipBounds();
		float x = board.translateXFromScreen((int) r.getX());
		float y = board.translateYFromScreen((int) r.getY());
		float w = board.translateRawXFromScreen((int) r.getWidth());
		float h = board.translateRawYFromScreen((int) r.getHeight());

		for (int line = 0; line < getLines(); line++) {
			int min = grid
					.getLowerLimit(line, (int) Math.floor(x),
							(int) Math.floor(y), (int) Math.ceil(w),
							(int) Math.ceil(h));
			int max = grid
					.getUpperLimit(line, (int) Math.floor(x),
							(int) Math.floor(y), (int) Math.ceil(w),
							(int) Math.ceil(h));
			drawGrid(g2d, board, line, min, max);
		}

	}

	/**
	 * Sets color of ordinary line of gridline line
	 * 
	 * @param line
	 * @param color
	 */
	public void setColor(int line, Color color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		infos.get(line).color = color;
	}

	/**
	 * Sets colors of ordinary lines of all gridlines
	 * 
	 * @param color
	 */
	public void setColors(Color color) {
		for (int line = 0; line < getLines(); line++) {
			setColor(line, color);
		}
	}

	/**
	 * Sets color of main line of gridline line
	 * 
	 * @param line
	 * @param color
	 */
	public void setMainColor(int line, Color color) {
		if (color == null) {
			throw new IllegalArgumentException();
		}
		infos.get(line).mainColor = color;
	}

	/**
	 * Sets colors of main lines of all gridlines
	 * 
	 * @param color
	 */
	public void setMainColors(Color color) {
		for (int line = 0; line < getLines(); line++) {
			setMainColor(line, color);
		}
	}

	/**
	 * Sets stroke of main line of gridline line
	 * 
	 * @param line
	 * @param stroke
	 */
	public void setMainStroke(int line, Stroke stroke) {
		if (stroke == null) {
			throw new IllegalArgumentException();
		}
		infos.get(line).mainStroke = stroke;
	}

	/**
	 * Sets stroke of main lines of all gridlines
	 * 
	 * @param stroke
	 */
	public void setMainStrokes(Stroke stroke) {
		for (int line = 0; line < getLines(); line++) {
			setMainStroke(line, stroke);
		}
	}

	/**
	 * Sets start offset of main line of gridline line
	 * 
	 * @param line
	 * @param offset
	 */
	public void setOffset(int line, int offset) {
		infos.get(line).offset = offset;
	}

	/**
	 * Sets start offsets of main lines of all gridlines
	 * 
	 * @param line
	 * @param offset
	 */
	public void setOffsets(int offset) {
		for (int line = 0; line < getLines(); line++) {
			setOffset(line, offset);
		}
	}

	/**
	 * Sets repetition of main lines of gridline line
	 * 
	 * @param line
	 * @param repetition
	 */
	public void setRepetition(int line, int repetition) {
		if (repetition <= 0) {
			throw new IllegalArgumentException();
		}
		infos.get(line).repetition = repetition;
	}

	/**
	 * Sets repetitions of main lines of all gridlines
	 * 
	 * @param line
	 * @param repetition
	 */
	public void setRepetitions(int repetition) {
		for (int line = 0; line < getLines(); line++) {
			setRepetition(line, repetition);
		}
	}

	/**
	 * Sets stroke of ordinary line of gridline line
	 * 
	 * @param line
	 * @param stroke
	 */
	public void setStroke(int line, Stroke stroke) {
		if (stroke == null) {
			throw new IllegalArgumentException();
		}
		infos.get(line).stroke = stroke;
	}

	/**
	 * Sets stroke of orinary lines of all gridlines
	 * 
	 * @param stroke
	 */
	public void setStroked(Stroke stroke) {
		for (int line = 0; line < getLines(); line++) {
			setStroke(line, stroke);
		}
	}

	/**
	 * Tests if this parallel line of gridline line is main line
	 * 
	 * @param line
	 * @param parallel
	 * @return
	 */
	private boolean isMainLine(int line, int parallel) {
		return (parallel - getOffset(line)) % getRepetition(line) == 0;
	}

	/**
	 * Draws gridline line in range from min to max
	 * 
	 * @param g2d
	 * @param board
	 * @param line
	 * @param min
	 * @param max
	 */
	protected void drawGrid(Graphics2D g2d, GameBoard board, int line, int min,
			int max) {
		GridLine gridLine = board.getBoard().getGrid().getGridLine(line);
		for (int i = min; i <= max; i++) {
			if (isMainLine(line, i)) {
				g2d.setColor(getMainColor(line));
				g2d.setStroke(getMainStroke(line));
			} else {
				g2d.setColor(getColor(line));
				g2d.setStroke(getStroke(line));
			}
			drawLine(g2d, board, gridLine.getLine(i));
		}
	}

	/**
	 * Draws particular line of gridline
	 * 
	 * @param g2d
	 * @param board
	 * @param line
	 */
	protected void drawLine(Graphics2D g2d, GameBoard board, Line line) {
		Rectangle r = g2d.getClipBounds();
		Point2D.Float p1 = new Point2D.Float(board.translateXToScreen(line
				.getP1().getX()), board.translateYToScreen(line.getP1().getY()));
		Point2D.Float p2 = new Point2D.Float(board.translateXToScreen(line
				.getP2().getX()), board.translateYToScreen(line.getP2().getY()));

		double dx = p1.getX() - p2.getX();
		double x1 = (r.getMinX() - p1.getX()) / dx;
		double x2 = (r.getMaxX() - p1.getX()) / dx;

		double dy = p1.getY() - p2.getY();
		double y1 = (r.getMinY() - p1.getY()) / dy;
		double y2 = (r.getMaxY() - p1.getY()) / dy;

		int x = (int) Math.ceil(Math.max(Math.abs(x1), Math.abs(x2)));
		int y = (int) Math.ceil(Math.max(Math.abs(y1), Math.abs(y2)));

		int z = Math.min(x, y) + 2;

		line = line.extend(z);
		p1 = new Point2D.Float(board.translateXToScreen(line.getP1().getX()),
				board.translateYToScreen(line.getP1().getY()));
		p2 = new Point2D.Float(board.translateXToScreen(line.getP2().getX()),
				board.translateYToScreen(line.getP2().getY()));
		g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
				(int) p2.getY());
	}

}
