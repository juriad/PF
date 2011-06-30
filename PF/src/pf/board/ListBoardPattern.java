package pf.board;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

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

	public static BoardPattern createListBoardPattern(Board board,
			Set<PointsEdge> pes) {
		return new ListBoardPattern(board, pes);
	}

	public ListBoardPattern(Board board, File f) throws FileNotFoundException {
		super(board, f);

	}

	public ListBoardPattern(Board board, Set<PointsEdge> pes) {
		super(board, pes);
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
			s.nextLine();
			addEdge(PointImpl.fromString(p1), PointImpl.fromString(p2));
		}
	}

	@Override
	public void save(BufferedWriter w) throws IOException {
		for (PointsEdge pe : this) {
			w.write(pe.p1 + " " + pe.p2);
			w.newLine();
		}
	}

}
