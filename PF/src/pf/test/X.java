package pf.test;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

import pf.board.Board;
import pf.board.BoardImpl;
import pf.gui.EdgesPainterImpl;
import pf.gui.GridPainterImpl;
import pf.gui.InteractiveBoard;
import pf.gui.VerticesPainterImpl;

public class X {

	public static void main(String[] args) throws FileNotFoundException {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		File f = new File("/home/adam/srgrid.txt");
		Board b = BoardImpl.createBoard(f);
		InteractiveBoard gb = new InteractiveBoard(b);
		gb.setVerticesPainterAndPaint(new VerticesPainterImpl(gb.getBoard()
				.getGrid().getGridType(),
				VerticesPainterImpl.DegreeType.BY_UNUSED));
		gb.setEdgesPainterAndPaint(new EdgesPainterImpl());
		gb.setGridPainterAndPaint(new GridPainterImpl(gb.getBoard().getGrid()
				.getGridType()));
		gb.setPreferredSize(new Dimension(800, 600));
		frame.getContentPane().add(gb);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
	}

}
