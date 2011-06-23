package pf.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import pf.analytics.Point;
import pf.analytics.PointImpl;

public class ComplexBoardPattern extends AbstractBoardPattern {

	public ComplexBoardPattern(Board board, GridPattern gp, File f)
			throws FileNotFoundException {
		super();
		switch (gp) {
		case COMPLEX_LIST:
			readFromFile(f);
			break;
		default:
			throw new IllegalStateException();
		}
	}

	protected void readFromFile(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		s.nextLine();
		String p1, p2;
		while (s.hasNextLine()) {
			p1 = s.findInLine(Point.pattern);
			p2 = s.findInLine(Point.pattern);
			addEdge(PointImpl.read(p1), PointImpl.read(p2));
		}
	}

}
