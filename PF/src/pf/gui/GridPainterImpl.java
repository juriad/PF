package pf.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import pf.analytics.Line;
import pf.board.Grid;
import pf.board.GridType;

public class GridPainterImpl implements GridPainter {

	protected class Info {
		final int line;
		Color color = Color.YELLOW.brighter();
		Color mainColor = Color.YELLOW;
		Stroke stroke = new BasicStroke();
		Stroke mainStroke = new BasicStroke(2);
		int offset = 0;
		int repetition = 5;

		public Info(int line) {
			this.line = line;
		}
	}

	private final GridType gt;

	protected List<Info> infos;

	public GridPainterImpl(GridType gt) {
		this.gt = gt;
		this.infos = new ArrayList<Info>();
		for (int i = 0; i < getLines(); i++)
			infos.add(new Info(i));
	}

	protected void drawGrid(Graphics2D g2d, GameBoard board, int line, int min,
			int max) {
		for (int i = min; i <= max; i++) {
			if (isMainLine(line, i)) {
				g2d.setColor(getMainColor(line));
				g2d.setStroke(getMainStroke(line));
			} else {
				g2d.setColor(getColor(line));
				g2d.setStroke(getStroke(line));
			}
			drawLine(g2d, board.getBoard().getGrid().getGridLine(line));
		}
	}

	protected void drawLine(Graphics2D g2d, Line line) {
		Rectangle r = g2d.getClipBounds();
		// TODO drawLine:
	}

	public Color getColor(int line) {
		return infos.get(line).color;
	}

	public GridType getGridType() {
		return gt;
	}

	public int getLines() {
		return gt.getLines();
	}

	public Color getMainColor(int line) {
		return infos.get(line).mainColor;
	}

	public Stroke getMainStroke(int line) {
		return infos.get(line).mainStroke;
	}

	public int getOffset(int line) {
		return infos.get(line).offset;
	}

	public int getRepetition(int line) {
		return infos.get(line).repetition;
	}

	public Stroke getStroke(int line) {
		return infos.get(line).stroke;
	}

	private boolean isMainLine(int line, int parallel) {
		return (parallel - getOffset(line)) % getRepetition(line) == 0;
	}

	@Override
	public void paintGrid(Graphics2D g2d, GameBoard board) {
		if (board.getBoard().getGrid().getGridType() != getGridType())
			throw new IllegalArgumentException();
		Grid grid = board.getBoard().getGrid();

		Rectangle r = g2d.getClipBounds();
		float x = board.translateXFromScreen((int) r.getX());
		float y = board.translateXFromScreen((int) r.getY());
		float w = board.translateXFromScreen((int) r.getWidth());
		float h = board.translateXFromScreen((int) r.getHeight());

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

	public void setColor(int line, Color color) {
		if (color == null)
			throw new IllegalArgumentException();
		infos.get(line).color = color;
	}

	public void setColors(Color color) {
		for (int line = 0; line < getLines(); line++)
			setColor(line, color);
	}

	public void setMainColor(int line, Color color) {
		if (color == null)
			throw new IllegalArgumentException();
		infos.get(line).mainColor = color;
	}

	public void setMainColors(Color color) {
		for (int line = 0; line < getLines(); line++)
			setMainColor(line, color);
	}

	public void setMainStroke(int line, Stroke stroke) {
		if (stroke == null)
			throw new IllegalArgumentException();
		infos.get(line).mainStroke = stroke;
	}

	public void setMainStrokes(Stroke stroke) {
		for (int line = 0; line < getLines(); line++)
			setMainStroke(line, stroke);
	}

	public void setOffset(int line, int offset) {
		infos.get(line).offset = offset;
	}

	public void setOffsets(int offset) {
		for (int line = 0; line < getLines(); line++)
			setOffset(line, offset);
	}

	public void setRepetition(int line, int repetition) {
		if (repetition <= 0)
			throw new IllegalArgumentException();
		infos.get(line).repetition = repetition;
	}

	public void setRepetitions(int repetition) {
		for (int line = 0; line < getLines(); line++)
			setRepetition(line, repetition);
	}

	public void setStroke(int line, Stroke stroke) {
		if (stroke == null)
			throw new IllegalArgumentException();
		infos.get(line).stroke = stroke;
	}

	public void setStroked(Stroke stroke) {
		for (int line = 0; line < getLines(); line++)
			setStroke(line, stroke);
	}

}
