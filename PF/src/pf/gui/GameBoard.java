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
	private Board board;
	private int horizontalPadding = 10;
	private int verticalPadding = 10;

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

	public int getHorizontalPadding() {
		return horizontalPadding;
	}

	@Override
	public Dimension getPreferredSize() {
		float unit = unitSize
				/ getBoard().getGrid().getGridType().getUnitSize();
		return new Dimension((int) (board.getWidth() * unit)
				+ getHorizontalPadding() * 2, (int) (board.getHeight() * unit)
				+ getVerticalPadding() * 2);
	}

	public int getVerticalPadding() {
		return verticalPadding;
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
		if (isPaintEdges() && edgesPainter != null)
			edgesPainter.paintEdges(g2d, this);
	}

	protected void paintGrid(Graphics2D g2d) {
		if (isPaintGrid() && gridPainter != null)
			gridPainter.paintGrid(g2d, this);
	}

	protected void paintVertices(Graphics2D g2d) {
		if (isPaintVertices() && vp != null)
			vp.paintVertices(g2d, this);
	}

	public void setEdgesPainter(EdgesPainter painter) {
		this.edgesPainter = painter;
	}

	public void setEdgesPainterAndPaint(EdgesPainter painter) {
		setEdgesPainter(painter);
		setPaintEdges(true);
	}

	public void setGridPainter(GridPainter painter) {
		this.gridPainter = painter;
	}

	public void setGridPainterAndPaint(GridPainter painter) {
		setGridPainter(painter);
		setPaintGrid(true);
	}

	public void setHorizontalPadding(int padding) {
		this.horizontalPadding = padding;
	}

	public void setPadding(int padding) {
		setHorizontalPadding(padding);
		setVerticalPadding(padding);
	}

	public void setPaintEdges(boolean paint) {
		this.paintEdges = paint;
	}

	public void setPaintGrid(boolean paint) {
		this.paintGrid = paint;
	}

	public void setPaintVertices(boolean paint) {
		this.paintVertices = paint;
	}

	public void setVerticalPadding(int padding) {
		this.verticalPadding = padding;
	}

	public void setVerticesPainter(VerticesPainter painter) {
		this.vp = painter;
	}

	public void setVerticesPainterAndPaint(VerticesPainter painter) {
		setVerticesPainter(painter);
		setPaintVertices(true);
	}

	public float translateRawXFromScreen(int x) {
		return x * ((float) getBoard().getWidth())
				/ (getWidth() - 2 * getHorizontalPadding());
	}

	public int translateRawXToScreen(float x) {
		return (int) ((x * (getWidth() - 2 * getHorizontalPadding())) / getBoard()
				.getWidth());
	}

	public float translateRawYFromScreen(int y) {
		return y * ((float) getBoard().getHeight())
				/ ((float) getHeight() - 2 * getVerticalPadding());
	}

	public int translateRawYToScreen(float y) {
		return (int) (y * (getHeight() - 2 * getVerticalPadding()) / getBoard()
				.getHeight());
	}

	public float translateXFromScreen(int x) {
		return translateRawXFromScreen(x - getHorizontalPadding());
	}

	public int translateXToScreen(float x) {
		return translateRawXToScreen(x) + getHorizontalPadding();
	}

	public float translateYFromScreen(int y) {
		return translateRawYFromScreen(y - getVerticalPadding());
	}

	public int translateYToScreen(float y) {
		return translateRawYToScreen(y) + getVerticalPadding();
	}
}
