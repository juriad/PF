package pf.interactive;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import pf.graph.Path;
import pf.graph.PathImpl;

public class PathPainterImpl implements PathPainter {
	public static class PathPainterImplFactory implements PathPainterFactory {

		private static volatile PathPainterImplFactory instance = null;

		public static PathPainterImplFactory getInstance() {
			if (instance == null) {
				synchronized (PathPainterImplFactory.class) {
					if (instance == null) {
						instance = new PathPainterImplFactory();
					}
				}
			}
			return instance;
		}

		private PathPainterImplFactory() {
		}

		@Override
		public PathPainter newInstance(InteractiveBoard board, Path path) {
			return new PathPainterImpl(board, path);
		}
	}

	protected final InteractiveBoard board;
	protected float radius = 0.2f;
	protected Color color = Color.BLUE;
	protected BasicStroke stroke = new BasicStroke();
	private final Path path;

	private Path localPath;

	public PathPainterImpl(InteractiveBoard board, Path path) {
		this.board = board;
		this.path = path;
		localPath = new PathImpl();
		localPath.extend(path);
	}

	@Override
	public Rectangle getBounds() {
		// TODO autogen method: getBounds
		return null;
	}

	public Color getColor() {
		return color;
	}

	public float getCornerRadius() {
		return radius;
	}

	@Override
	public Rectangle getDiffBounds() {
		// TODO autogen method: getDiffBounds
		return null;
	}

	@Override
	public PathPainterFactory getFactory() {
		return PathPainterImplFactory.getInstance();
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	@Override
	public void paintPath(Graphics2D g2d, InteractiveBoard interactiveBoard,
			Path p) {
		// TODO autogen method: paintPath

	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setCornerRadius(float radius) {
		this.radius = radius;
	}

	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

}
