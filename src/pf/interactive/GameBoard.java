package pf.interactive;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import pf.board.Board;

/**
 * 
 * Draws a game board with a {@link Board} as its model. It can draw vertices,
 * edges, grid. This class doesn't support any kind of user interaction. It
 * provides many translation methods between model and component coordinates.
 * 
 * @author Adam Juraszek
 * 
 */
/**
 * @author Adam Juraszek
 * 
 */
public class GameBoard extends JComponent {
	private static final long serialVersionUID = 1L;
	private VerticesPainter vp = null;
	private boolean paintVertices = false;
	private GridPainter gridPainter = null;
	private boolean paintGrid = false;
	private EdgesPainter edgesPainter = null;
	private boolean paintEdges = false;
	protected Board board;
	private int paddingTop = 10;
	private int paddingLeft = 10;
	private int paddingBottom = 10;
	private int paddingRight = 10;

	protected static final int unitSize = 100;

	/**
	 * It is very useful to have a game board without any model assigned.
	 */
	public GameBoard() {
		board = null;
	}

	/**
	 * @return model of this game board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @return size which could be prefered, depends only on size of the model
	 */
	public Dimension getCountedSize() {
		if (getBoard() == null) {
			return new Dimension(getPaddingLeft() + getPaddingRight(),
					getPaddingTop() + getPaddingTop());
		}
		float unit = unitSize
				/ getBoard().getGrid().getGridType().getUnitSize();
		return new Dimension((int) (board.getWidth() * unit) + getPaddingLeft()
				+ getPaddingRight(), (int) (board.getHeight() * unit)
				+ getPaddingTop() + getPaddingBottom());
	}

	/**
	 * @return painter which paints edges
	 */
	public EdgesPainter getEdgesPainter() {
		return edgesPainter;
	}

	/**
	 * @return painter which paints grid
	 */
	public GridPainter getGridPainter() {
		return gridPainter;
	}

	/**
	 * @return distance between bottom-most vertex and bottom edge
	 */
	public int getPaddingBottom() {
		return paddingBottom;
	}

	/**
	 * @return distance between left-most vertex and left edge
	 */
	public int getPaddingLeft() {
		return paddingLeft;
	}

	/**
	 * @return distance between right-most vertex and right edge
	 */
	public int getPaddingRight() {
		return paddingRight;
	}

	/**
	 * @return distance between top-most vertex and top edge
	 */
	public int getPaddingTop() {
		return paddingTop;
	}

	/**
	 * @return painter which paints vertices
	 */
	public VerticesPainter getVerticesPainter() {
		return vp;
	}

	/**
	 * Painting depends on this and assigned painter which must be not null
	 * 
	 * @return true if should paint edges, doesn't mean edges will be painted
	 */
	public boolean isPaintEdges() {
		return paintEdges;
	}

	/**
	 * Painting depends on this and assigned painter which must be not null
	 * 
	 * @return true if should paint grid, doesn't mean grid will be painted
	 */
	public boolean isPaintGrid() {
		return paintGrid;
	}

	/**
	 * Painting depends on this and assigned painter which must be not null
	 * 
	 * @return true if should paint vertices, doesn't mean vertices will be
	 *         painted
	 */
	public boolean isPaintVertices() {
		return paintVertices;
	}

	/**
	 * Sets a model of this game board.
	 * 
	 * @param board
	 *            new model
	 */
	public void setBoard(Board board) {
		this.board = board;
		setVerticesPainter(null);
		setPaintVertices(false);
		setEdgesPainter(null);
		setPaintEdges(false);
		setGridPainter(null);
		setPaintGrid(false);
		repaint();
	}

	/**
	 * Sets a painter of edges, if painter is null, edges will not be painted.
	 * 
	 * @param painter
	 *            painter responsible for painting edges
	 */
	public void setEdgesPainter(EdgesPainter painter) {
		edgesPainter = painter;
	}

	/**
	 * Sets a painter of edges and also sets paintEdges to true. If painter is
	 * null, edges will not be painted
	 * 
	 * @param painter
	 *            painter responsible for painting edges
	 */
	public void setEdgesPainterAndPaint(EdgesPainter painter) {
		setEdgesPainter(painter);
		setPaintEdges(true);
	}

	/**
	 * Sets a painter of grid, if painter is null, grid will not be painted.
	 * 
	 * @param painter
	 *            painter responsible for painting grid
	 */
	public void setGridPainter(GridPainter painter) {
		gridPainter = painter;
	}

	/**
	 * Sets a painter of grid and also sets paintGrid to true. If painter is
	 * null, grid will not be painted.
	 * 
	 * @param painter
	 *            painter responsible for painting grid
	 */
	public void setGridPainterAndPaint(GridPainter painter) {
		setGridPainter(painter);
		setPaintGrid(true);
	}

	/**
	 * Sets left and right padding to the same value.
	 * 
	 * @param padding
	 */
	public void setHorizontalPadding(int padding) {
		setPaddingLeft(padding);
		setPaddingRight(padding);
	}

	/**
	 * Sets all paddings to the same value.
	 * 
	 * @param padding
	 */
	public void setPadding(int padding) {
		setHorizontalPadding(padding);
		setVerticalPadding(padding);
	}

	/**
	 * Sets bottom padding.
	 * 
	 * @param padding
	 */
	public void setPaddingBottom(int padding) {
		if (padding < 0) {
			throw new IllegalArgumentException();
		}
		paddingBottom = padding;
	}

	/**
	 * Sets left padding.
	 * 
	 * @param padding
	 */
	public void setPaddingLeft(int padding) {
		if (padding < 0) {
			throw new IllegalArgumentException();
		}
		paddingLeft = padding;
	}

	/**
	 * Sets right padding.
	 * 
	 * @param padding
	 */
	public void setPaddingRight(int padding) {
		if (padding < 0) {
			throw new IllegalArgumentException();
		}
		paddingRight = padding;
	}

	/**
	 * Sets top padding.
	 * 
	 * @param padding
	 */
	public void setPaddingTop(int padding) {
		if (padding < 0) {
			throw new IllegalArgumentException();
		}
		paddingTop = padding;
	}

	/**
	 * Sets whether to paint edges. Setting true doesn't mean, it will be
	 * painted, it depends also on painter.
	 * 
	 * @param paint
	 */
	public void setPaintEdges(boolean paint) {
		paintEdges = paint;
	}

	/**
	 * Sets whether to paint grid. Setting true doesn't mean, it will be
	 * painted, it depends also on painter.
	 * 
	 * @param paint
	 */
	public void setPaintGrid(boolean paint) {
		paintGrid = paint;
	}

	/**
	 * Sets whether to paint vertices. Setting true doesn't mean, it will be
	 * painted, it depends also on painter.
	 * 
	 * @param paint
	 */
	public void setPaintVertices(boolean paint) {
		paintVertices = paint;
	}

	/**
	 * Sets top and bottom padding to the same value.
	 * 
	 * @param padding
	 */
	public void setVerticalPadding(int padding) {
		setPaddingTop(padding);
		setPaddingBottom(padding);
	}

	/**
	 * Sets a painter of vertices, if painter is null, vertices will not be
	 * painted.
	 * 
	 * @param painter
	 *            painter responsible for painting vertices
	 */
	public void setVerticesPainter(VerticesPainter painter) {
		vp = painter;
	}

	/**
	 * Sets a painter of vertices and also sets paintVertices to true. If
	 * painter is null, vertices will not be painted.
	 * 
	 * @param painter
	 *            painter responsible for painting vertices
	 */
	public void setVerticesPainterAndPaint(VerticesPainter painter) {
		setVerticesPainter(painter);
		setPaintVertices(true);
	}

	/**
	 * Translates horizontal distance of x pixels to model coordinate system
	 * 
	 * @param x
	 * @return translated x
	 */
	public float translateRawXFromScreen(int x) {
		return x * ((float) getBoard().getWidth())
				/ (getWidth() - getPaddingLeft() - getPaddingRight());
	}

	/**
	 * Translates horizontal distance of model to screen coordinate system
	 * 
	 * @param x
	 * @return translated x
	 */
	public int translateRawXToScreen(float x) {
		return (int) ((x * (getWidth() - getPaddingLeft() - getPaddingRight())) / getBoard()
				.getWidth());
	}

	/**
	 * Translates vertical distance of x pixels to model coordinate system
	 * 
	 * @param y
	 * @return translated y
	 */
	public float translateRawYFromScreen(int y) {
		return y * ((float) getBoard().getHeight())
				/ ((float) getHeight() - getPaddingTop() - getPaddingBottom());
	}

	/**
	 * Translates vertical distance of model to screen coordinate system
	 * 
	 * @param y
	 * @return translated y
	 */
	public int translateRawYToScreen(float y) {
		return (int) (y * (getHeight() - getPaddingTop() - getPaddingBottom()) / getBoard()
				.getHeight());
	}

	/**
	 * Translates x value of point on screen to model coordinate system
	 * 
	 * @param x
	 * @return translated x
	 */
	public float translateXFromScreen(int x) {
		return translateRawXFromScreen(x - getPaddingLeft());
	}

	/**
	 * Translates x value of point in model to screen coordinate system
	 * 
	 * @param x
	 * @return translated x
	 */
	public int translateXToScreen(float x) {
		return translateRawXToScreen(x) + getPaddingLeft();
	}

	/**
	 * Translates y value of point on screen to model coordinate system
	 * 
	 * @param y
	 * @return translated y
	 */
	public float translateYFromScreen(int y) {
		return translateRawYFromScreen(y - getPaddingTop());
	}

	/**
	 * Translates y value of point in model to screen coordinate system
	 * 
	 * @param y
	 * @return translated y
	 */
	public int translateYToScreen(float y) {
		return translateRawYToScreen(y) + getPaddingTop();
	}

	/**
	 * Adds paintGrid, paintEdges, paintVertices to be painted
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		paintGrid(g2d);
		paintEdges(g2d);
		paintVertices(g2d);
	}

	/**
	 * Calls edges painter to paint edges if desired
	 * 
	 * @param g2d
	 */
	protected void paintEdges(Graphics2D g2d) {
		if (isPaintEdges() && edgesPainter != null) {
			edgesPainter.paintEdges(g2d, this);
		}
	}

	/**
	 * Calls grid painter to paint grid if desired
	 * 
	 * @param g2d
	 */
	protected void paintGrid(Graphics2D g2d) {
		if (isPaintGrid() && gridPainter != null) {
			gridPainter.paintGrid(g2d, this);
		}
	}

	/**
	 * Calls vertices painter to paint vertices if desired
	 * 
	 * @param g2d
	 */
	protected void paintVertices(Graphics2D g2d) {
		if (isPaintVertices() && vp != null) {
			vp.paintVertices(g2d, this);
		}
	}
}
