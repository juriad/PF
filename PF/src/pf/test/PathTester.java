package pf.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.swing.JFrame;

import pf.board.Board;
import pf.board.BoardImpl;
import pf.graph.Edge;
import pf.graph.Path;
import pf.graph.PathImpl;
import pf.graph.Vertex;
import pf.interactive.GridPainterImpl;
import pf.interactive.InteractiveBoard;
import pf.interactive.PathPainterImpl;
import pf.interactive.VerticesPainterImpl;
import pf.interactive.VerticesPainterImpl.DegreeType;

public class PathTester {

	public static void main(String[] args) throws FileNotFoundException {
		Board b = BoardImpl.createBoard(new File("/home/adam/ftrgrid.txt"));

		Iterator<Edge> ei = b.getGraph().edgesIterator(false);
		while (ei.hasNext()) {
			System.out.println(ei.next());
		}

		Iterator<Vertex> vi = b.getGraph().verticesIterator();
		while (vi.hasNext()) {
			System.out.println(vi.next());
		}

		Path p = new PathImpl();
		Vertex v1 = b.getVertex(1, 2);
		Vertex v2 = b.getVertex(1, 4);
		Edge e1 = v1.edgeToVertex(v2);
		p.extend(e1);
		Vertex v3 = b.getVertex(0, 4);
		Edge e2 = v2.edgeToVertex(v3);
		p.extend(e2);
		Vertex v4 = b.getVertex(1, 6);
		Edge e3 = v3.edgeToVertex(v4);
		p.extend(e3);
		System.out.println("before ib");
		InteractiveBoard ib = new InteractiveBoard(b);
		ib.setPadding(0);
		ib.setVerticesPainterAndPaint(new VerticesPainterImpl(b.getGrid()
				.getGridType(), DegreeType.BY_UNUSED));
		ib.setGridPainterAndPaint(new GridPainterImpl(b.getGrid().getGridType()));
		ib.addPath(p);
		System.out.println("before pp");
		PathPainterImpl pp = new PathPainterImpl(ib, p);
		ib.setPathPainter(p, pp);
		ib.setPaintPaths(true);

		System.out.println("frame");
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(ib);
		f.pack();
		f.setVisible(true);
	}
}
