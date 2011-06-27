package pf.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import pf.board.Board;

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

	public GameBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public EdgesPainter getEdgesPainter() {
		return edgesPainter;
	}

	public GridPainter getGridPainter() {
		return gridPainter;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public int getPaddingTop() {
		return paddingTop;
	}

	@Override
	public Dimension getPreferredSize() {
		float unit = unitSize
				/ getBoard().getGrid().getGridType().getUnitSize();
		return new Dimension((int) (board.getWidth() * unit) + getPaddingLeft()
				+ getPaddingRight(), (int) (board.getHeight() * unit)
				+ getPaddingTop() + getPaddingBottom());
	}

	public VerticesPainter getVerticesPainter() {
		return vp;
	}

	public boolean isPaintEdges() {
		return paintEdges;
	}

	public boolean isPaintGrid() {
		return paintGrid;
	}

	public boolean isPaintVertices() {
		return paintVertices;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// System.out.println("paint");
		paintGrid(g2d);
		paintVertices(g2d);
		paintEdges(g2d);
	}

	protected void paintEdges(Graphics2D g2d) {
		if (isPaintEdges() && edgesPainter != null) {
			edgesPainter.paintEdges(g2d, this);
		}
	}

	protected void paintGrid(Graphics2D g2d) {
		if (isPaintGrid() && gridPainter != null) {
			gridPainter.paintGrid(g2d, this);
		}
	}

	protected void paintVertices(Graphics2D g2d) {
		if (isPaintVertices() && vp != null) {
			vp.paintVertices(g2d, this);
		}
	}

	public void setEdgesPainter(EdgesPainter painter) {
		edgesPainter = painter;
	}

	public void setEdgesPainterAndPaint(EdgesPainter painter) {
		setEdgesPainter(painter);
		setPaintEdges(true);
	}

	public void setGridPainter(GridPainter painter) {
		gridPainter = painter;
	}

	public void setGridPainterAndPaint(GridPainter painter) {
		setGridPainter(painter);
		setPaintGrid(true);
	}

	public void setHorizontalPadding(int padding) {
		setPaddingLeft(padding);
		setPaddingRight(padding);
	}

	public void setPadding(int padding) {
		setHorizontalPadding(padding);
		setVerticalPadding(padding);
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public void setPaddingLeft(int padding) {
		paddingLeft = padding;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public void setPaddingTop(int padding) {
		paddingTop = padding;
	}

	public void setPaintEdges(boolean paint) {
		paintEdges = paint;
	}

	public void setPaintGrid(boolean paint) {
		paintGrid = paint;
	}

	public void setPaintVertices(boolean paint) {
		paintVertices = paint;
	}

	public void setVerticalPadding(int padding) {
		setPaddingTop(padding);
		setPaddingBottom(padding);
	}

	public void setVerticesPainter(VerticesPainter painter) {
		vp = painter;
	}

	public void setVerticesPainterAndPaint(VerticesPainter painter) {
		setVerticesPainter(painter);
		setPaintVertices(true);
	}

	public float translateRawXFromScreen(int x) {
		return x * ((float) getBoard().getWidth())
				/ (getWidth() - getPaddingLeft() - getPaddingRight());
	}

	public int translateRawXToScreen(float x) {
		return (int) ((x * (getWidth() - getPaddingLeft() - getPaddingRight())) / getBoard()
				.getWidth());
	}

	public float translateRawYFromScreen(int y) {
		return y * ((float) getBoard().getHeight())
				/ ((float) getHeight() - getPaddingTop() - getPaddingBottom());
	}

	public int translateRawYToScreen(float y) {
		return (int) (y * (getHeight() - getPaddingTop() - getPaddingBottom()) / getBoard()
				.getHeight());
	}

	public float translateXFromScreen(int x) {
		return translateRawXFromScreen(x - getPaddingLeft());
	}

	public int translateXToScreen(float x) {
		return translateRawXToScreen(x) + getPaddingLeft();
	}

	public float translateYFromScreen(int y) {
		return translateRawYFromScreen(y - getPaddingTop());
	}

	public int translateYToScreen(float y) {
		return translateRawYToScreen(y) + getPaddingTop();
	}
}
