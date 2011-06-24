package pf.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import pf.analytics.Point;
import pf.analytics.PointImpl;

public class ListBoardPattern extends ComplexBoardPattern {

	public static BoardPattern createListBoardPattern(Board board, File f) {
		try {
			return new ListBoardPattern(board, f);
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	public ListBoardPattern(Board board, File f) throws FileNotFoundException {
		super(board, f);

	}

	@Override
	protected void readFromFile(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		s.nextLine();
		s.nextLine();
		String p1, p2;
		while (s.hasNextLine()) {
			p1 = s.findInLine(Point.pattern);
			p2 = s.findInLine(Point.pattern);
			addEdge(PointImpl.read(p1), PointImpl.read(p2));
		}
	}

}
