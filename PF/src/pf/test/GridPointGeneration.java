package pf.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import pf.analytics.Point;
import pf.analytics.PointImpl;
import pf.board.Board;
import pf.board.BoardImpl;
import pf.board.GridLine;
import pf.board.GridLineImpl;
import pf.graph.Vertex;

public class GridPointGeneration {
	public static void main(String[] args) throws FileNotFoundException {
		
		Point p1 = new PointImpl(0, 0);
		Point p2 = new PointImpl(11, 19);
		Point p3 = new PointImpl(22, 0);
		
		
		GridLine gl = new GridLineImpl(p1, p2, p3);
		
		System.out.println(gl.getNearest(new PointImpl(0, 100)));
		
		
		int i =1;
		if(i==1)
		//	return;
		;
		
		
		File f = new File("/home/adam/trgrid.txt");
		Board b = BoardImpl.createBoard(f);
		Iterator<Vertex> v = b.getGraph().verticesIterator();
		while(v.hasNext()) {
			System.out.println(v.next());
		}

	}
}
