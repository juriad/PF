package pf.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import pf.board.Board;
import pf.board.BoardImpl;
import pf.graph.Edge;
import pf.graph.Vertex;

public class GridPointGeneration {
	public static void main(String[] args) throws FileNotFoundException {
		File f = new File("/home/adam/srgrid.txt");
		Board b = BoardImpl.createBoard(f);
		Iterator<Vertex> vi = b.getGraph().verticesIterator();
		Vertex v;
		int i = 0;
		while (vi.hasNext()) {
			v = vi.next();
			System.out.println(v);

			Iterator<Edge> e = v.edgesIterator(false);
			while (e.hasNext()) {
				i++;
				System.out.println(e.next());
			}
		}
		System.out.println(i);

		Iterator<Edge> e = b.getGraph().edgesIterator(false);
		i = 0;
		while (e.hasNext()) {
			i++;
			System.out.println(e.next());
		}
		System.out.println(i);

	}
}
