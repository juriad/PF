package pf.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import pf.board.Board;

public class GameBoard extends JComponent {
	private static final long serialVersionUID = 1L;
	private VerticesPainter vp = null;
	private boolean paintVertices = false;
	private GridPainter gp = null;
	private boolean paintGrid = false;
	private EdgesPainter ep = null;
	private boolean paintEdges = false;
	private Board board;

	public GameBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public EdgesPainter getEdgesPainter() {
		return ep;
	}

	public GridPainter getGridPainter() {
		return gp;
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

		paintGrid(g2d);
		paintVertices(g2d);
		paintEdges(g2d);
	}

	// TODO preffered size

	protected void paintEdges(Graphics2D g2d) {
		if (isPaintEdges() && ep != null)
			ep.paintEdges(g2d, this);
	}

	protected void paintGrid(Graphics2D g2d) {
		if (isPaintGrid() && gp != null)
			gp.paintGrid(g2d, this);
	}

	protected void paintVertices(Graphics2D g2d) {
		if (isPaintVertices() && vp != null)
			vp.paintVertices(g2d, this);
	}

	public void setEdgesPainter(EdgesPainter painter) {
		this.ep = painter;
	}

	public void setGridPainter(GridPainter painter) {
		this.gp = painter;
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

	public void setVerticesPainter(VerticesPainter painter) {
		this.vp = painter;
	}

	public float translateXFromScreen(int x) {
		return x * ((float) getBoard().getWidth()) / getWidth();
	}

	public int translateXToScreen(float x) {
		return (int) (x * getWidth() / getBoard().getWidth());
	}

	public float translateYFromScreen(int y) {
		return y * ((float) getBoard().getHeight()) / getHeight();
	}

	public int translateYToScreen(float y) {
		return (int) (y * getHeight() / getBoard().getHeight());
	}
}
