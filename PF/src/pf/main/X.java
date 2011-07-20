package pf.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import pf.board.Board;
import pf.board.BoardImpl;
import pf.board.GridPattern;
import pf.gui.InteractiveBoardControl;
import pf.interactive.EdgesPainterImpl;
import pf.interactive.GridPainterImpl;
import pf.interactive.InteractiveBoard;
import pf.interactive.TouchEvent;
import pf.interactive.TouchListener;
import pf.interactive.VerticesPainterImpl;

public class X {

	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		File f = new File("/home/adam/trgrid.txt");
		Board b = BoardImpl.createBoard(f);
		final InteractiveBoard gb = new InteractiveBoard();
		gb.setBoard(b);
		gb.setEditable(true);
		gb.setVerticesPainterAndPaint(new VerticesPainterImpl(gb.getBoard()
				.getGrid().getGridType(),
				VerticesPainterImpl.DegreeType.BY_UNUSED));
		EdgesPainterImpl ep = new EdgesPainterImpl();
		ep.setUsedStroke(new BasicStroke(3));
		gb.setEdgesPainterAndPaint(ep);
		gb.setGridPainterAndPaint(new GridPainterImpl(gb.getBoard().getGrid()
				.getGridType()));
		gb.setPreferredSize(new Dimension(800, 600));

		gb.setTouchActive(true);
		gb.setTouchReturnAllowed(true);
		gb.addTouchListener(new TouchListener() {

			@Override
			public void touchCancelled(TouchEvent e) {
				System.out.println("cancelled:");
				gb.repaint();
			}

			@Override
			public void touchEnded(TouchEvent e) {
				System.out.println("ended:");
				gb.repaint();
			}

			@Override
			public void touchLonger(TouchEvent e) {
				System.out.println("longer: " + e.getEdge());
				e.getEdge().setUsed(!e.getEdge().isUsed());
				gb.repaintEdge(e.getEdge());
			}

			@Override
			public void touchShorter(TouchEvent e) {
				System.out.println("shorter: " + e.getEdge());
				e.getEdge().setUsed(!e.getEdge().isUsed());
				gb.repaintEdge(e.getEdge());
			}

			@Override
			public void touchStarted(TouchEvent e) {
				System.out.println("started: " + e.getVertex());
				gb.repaintVertex(e.getVertex());
			}
		});

		InteractiveBoardControl ibc = new InteractiveBoardControl(gb);
		c.add(ibc, BorderLayout.NORTH);

		c.add(gb, BorderLayout.CENTER);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		gb.getBoard().save(new File("/home/adam/save"),
				GridPattern.COMPLEX_LIST);
	}

}
