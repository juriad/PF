package pf.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import pf.board.Board;
import pf.board.BoardImpl;
import pf.graph.Graph;
import pf.graph.Vertex;

public class GridPointGeneration {
	public static void main(String[] args) throws FileNotFoundException {
		File f = new File("/home/adam/srgrid.txt");
		Board b = BoardImpl.createBoard(f);
		System.out.println("created");

		Iterator<Graph> gi = b.getGraph().subGraphsIterator();
		while (gi.hasNext()) {
			Graph g = gi.next();
			Iterator<Vertex> vi = g.verticesIterator();
			int i = 0;
			while (vi.hasNext()) {
				i++;
				vi.next();
			}
			System.out.println(i);
		}

		Iterator<Vertex> vi = b.getGraph().verticesIterator();
		while (vi.hasNext())
			System.out.println(vi.next());

	}
}
