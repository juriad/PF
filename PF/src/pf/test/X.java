package pf.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import pf.board.Board;
import pf.board.BoardImpl;
import pf.gui.EdgesPainterImpl;
import pf.gui.GridPainterImpl;
import pf.gui.InteractiveBoard;
import pf.gui.InteractiveBoardControl;
import pf.gui.TouchEvent;
import pf.gui.TouchListener;
import pf.gui.VerticesPainterImpl;

public class X {

	public static void main(String[] args) throws FileNotFoundException {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		File f = new File("/home/adam/srgrid.txt");
		Board b = BoardImpl.createBoard(f);
		final InteractiveBoard gb = new InteractiveBoard(b);
		gb.setVerticesPainterAndPaint(new VerticesPainterImpl(gb.getBoard()
				.getGrid().getGridType(),
				VerticesPainterImpl.DegreeType.BY_UNUSED));
		gb.setEdgesPainterAndPaint(new EdgesPainterImpl());
		gb.setGridPainterAndPaint(new GridPainterImpl(gb.getBoard().getGrid()
				.getGridType()));
		gb.setPreferredSize(new Dimension(800, 600));

		gb.setTouchActive(true);
		gb.setTouchReturnAllowed(true);
		gb.addTouchListener(new TouchListener() {

			@Override
			public void touchCancelled(TouchEvent e) {
				System.out.println("cancelled:");
			}

			@Override
			public void touchEnded(TouchEvent e) {
				System.out.println("ended:");
			}

			@Override
			public void touchLonger(TouchEvent e) {
				System.out.println("longer: " + e.getEdge());
				e.getEdge().setUsed(!e.getEdge().isUsed());
				gb.repaint();
			}

			@Override
			public void touchShorter(TouchEvent e) {
				System.out.println("shorter: " + e.getEdge());
				e.getEdge().setUsed(!e.getEdge().isUsed());
				gb.repaint();
			}

			@Override
			public void touchStarted(TouchEvent e) {
				System.out.println("started: " + e.getVertex());

			}
		});

		c.add(new InteractiveBoardControl(gb), BorderLayout.NORTH);
		c.add(gb, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

}
