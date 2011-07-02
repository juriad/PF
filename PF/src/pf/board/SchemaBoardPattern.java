package pf.board;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import pf.analytics.PointImpl;
import pf.board.BoardPattern.PointsEdge;

public class SchemaBoardPattern {

	public static class DiagonalSchemaBoardPattern extends ComplexBoardPattern {

		protected DiagonalSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		protected DiagonalSchemaBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) throws IOException {
			char[][] schema = new char[getBoard().getWidth() * 2 + 1][getBoard()
					.getHeight() * 2 + 1];
			for (int i = 0; i < getBoard().getHeight() * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() * 2 + 1; j++) {
					if (i % 2 == 0 && j % 2 == 0) {
						schema[j][i] = '.';
					} else {
						schema[j][i] = ' ';
					}
				}
			}
			for (PointsEdge pe : this) {
				int x = pe.p1.getX() + pe.p2.getX();
				int y = pe.p1.getY() + pe.p2.getY();
				if (y % 2 == 0) {
					schema[x][y] = '-';
				} else if (x % 2 == 0) {
					schema[x][y] = '|';
				} else {
					char c = ' ';
					if (pe.p1.getX() * 2 > x && pe.p1.getY() * 2 > y
							|| pe.p1.getX() * 2 < x && pe.p1.getY() * 2 < y) {
						c = '\\';
					} else {
						c = '/';
					}
					if (schema[x][y] == '/' && c == '\\'
							|| schema[x][y] == '\\' && c == '/') {
						schema[x][y] = 'X';
					} else {
						schema[x][y] = c;
					}
				}
			}

			for (int i = 0; i < getBoard().getHeight() * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() * 2 + 1; j++) {
					w.append(schema[j][i]);
				}
				w.newLine();
			}
		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				switch (l % 2) {
				case 0:
					if (!line.matches("([. ][- ])*[. ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("-", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, l / 2), new PointImpl(ee + 1,
								l / 2));
					}
					break;
				case 1:
					if (!line.matches("([| ][\\\\/X ])*[| ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("|", e + 1)) >= 0) {
						ee = e / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee - 1, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee + 1, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("X", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee + 1, (l + 1) / 2));
						addEdge(new PointImpl(ee + 1, (l - 1) / 2),
								new PointImpl(ee, (l + 1) / 2));
					}
					break;
				}

				l++;
			}
		}
	}

	public static class DiagonalXSchemaBoardPattern extends ComplexBoardPattern {

		protected DiagonalXSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		protected DiagonalXSchemaBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) throws IOException {
			char[][] schema = new char[getBoard().getWidth() * 2 + 1][getBoard()
					.getHeight() * 2 + 1];
			for (int i = 0; i < getBoard().getHeight() * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() * 2 + 1; j++) {
					if (i % 2 == 0 && j % 4 == i % 4) {
						schema[j][i] = '.';
					} else {
						schema[j][i] = ' ';
					}
				}
			}
			for (PointsEdge pe : this) {
				int x = pe.p1.getX() + pe.p2.getX();
				int y = pe.p1.getY() + pe.p2.getY();
				if (y % 4 == 0) {
					schema[x][y] = '-';
				} else if (x % 2 == 0) {
					schema[x][y] = '|';
				} else {
					if (pe.p1.getX() * 2 > x && pe.p1.getY() * 2 > y
							|| pe.p1.getX() * 2 < x && pe.p1.getY() * 2 < y) {
						schema[x][y] = '\\';
					} else {
						schema[x][y] = '/';
					}
				}
			}

			for (int i = 0; i < getBoard().getHeight() * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() * 2 + 1; j++) {
					w.append(schema[j][i]);
				}
				w.newLine();
			}

		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				switch (l % 4) {
				case 0:
					if (!line.matches("([. ]((   )|(---)|( - )))*[. ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("---", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, l / 2), new PointImpl(ee + 2,
								l / 2));
					}
					e = -1;
					while ((e = line.indexOf(" - ", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, l / 2), new PointImpl(ee + 2,
								l / 2));
					}
					break;
				case 1:
					if (!line.matches("([| ][\\\\ ] [/ ])*[| ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee + 1, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee - 1, (l + 1) / 2));
					}
					break;
				case 2:
					if (!line.matches("([| ] [. ] )*[| ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("|", e + 1)) >= 0) {
						ee = e / 2;
						addEdge(new PointImpl(ee, l / 2 - 1), new PointImpl(ee,
								l / 2 + 1));
					}
					break;
				case 3:
					if (!line.matches("([| ][/ ] [\\\\ ])*[| ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee - 1, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee + 1, (l + 1) / 2));
					}
					break;
				}

				l++;
			}
		}
	}

	public static class SquareSchemaBoardPattern extends ComplexBoardPattern {

		protected SquareSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		protected SquareSchemaBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) throws IOException {
			char[][] schema = new char[getBoard().getWidth() * 2 + 1][getBoard()
					.getHeight() * 2 + 1];
			for (int i = 0; i < getBoard().getHeight() * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() * 2 + 1; j++) {
					if (i % 2 == 0 && j % 2 == 0) {
						schema[j][i] = '.';
					} else {
						schema[j][i] = ' ';
					}
				}
			}
			for (PointsEdge pe : this) {
				int x = pe.p1.getX() + pe.p2.getX();
				int y = pe.p1.getY() + pe.p2.getY();
				if (y % 2 == 0) {
					schema[x][y] = '-';
				} else {
					schema[x][y] = '|';
				}
			}

			for (int i = 0; i < getBoard().getHeight() * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() * 2 + 1; j++) {
					w.append(schema[j][i]);
				}
				w.newLine();
			}
		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				switch (l % 2) {
				case 0:
					if (!line.matches("([. ][- ])*[. ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("-", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, l / 2), new PointImpl(ee + 1,
								l / 2));
					}
					break;
				case 1:
					if (!line.matches("([| ] )*[| ]?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("|", e + 1)) >= 0) {
						ee = e / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee, (l + 1) / 2));
					}
					break;
				}

				l++;
			}
		}
	}

	public static class TriangleSchemaBoardPattern extends ComplexBoardPattern {

		protected TriangleSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		protected TriangleSchemaBoardPattern(Board board, Set<PointsEdge> pes) {
			super(board, pes);
		}

		@Override
		public void save(BufferedWriter w) throws IOException {
			char[][] schema = new char[getBoard().getWidth() / 2 + 1][getBoard()
					.getHeight() / 7 * 2 + 1];
			for (int i = 0; i < getBoard().getHeight() / 7 * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() / 2 + 1; j++) {
					if (i % 2 == 0 && j % 4 == i % 4) {
						schema[j][i] = '.';
					} else {
						schema[j][i] = ' ';
					}
				}
			}

			for (PointsEdge pe : this) {
				int x = pe.p1.getX() + pe.p2.getX();
				int y = pe.p1.getY() + pe.p2.getY();
				y = y / 7;
				x = x / 4;
				if (y % 2 == 0) {
					schema[x][y] = '-';
				} else {
					if (pe.p1.getX() / 2 > x && pe.p1.getY() * 2 > y
							|| pe.p1.getX() / 2 < x && pe.p1.getY() * 2 < y) {
						if (y % 4 == 1) {
							schema[x][y] = '\\';
						} else {
							schema[x][y] = '/';
						}
					} else {
						if (y % 4 == 1) {
							schema[x][y] = '/';
						} else {
							schema[x][y] = '\\';
						}
					}
				}
			}

			for (int i = 0; i < getBoard().getHeight() / 7 * 2 + 1; i++) {
				for (int j = 0; j < getBoard().getWidth() / 2 + 1; j++) {
					w.append(schema[j][i]);
				}
				w.newLine();
			}
		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				switch (l % 4) {
				case 0:
				case 2:
					if (!line
							.matches("(  )?([. ]((   )|(---)|( - )))*[. ](  )?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("---", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 8, l / 2 * 7));
					}
					e = -1;
					while ((e = line.indexOf(" - ", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 8, l / 2 * 7));
					}
					break;
				case 1:
					if (!line.matches("( [\\\\ ] [/ ])* ?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 4, (l + 1) / 2 * 7));
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee - 4, (l + 1) / 2 * 7));
					}
					break;
				case 3:
					if (!line.matches("( [/ ] [\\\\ ])* ?")) {
						throw new InputMismatchException();
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee - 4, (l + 1) / 2 * 7));
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 4, (l + 1) / 2 * 7));
					}
					break;
				}

				l++;
			}
		}
	}

	public static BoardPattern createSchemaBoardPattern(Board board, File f) {
		try {
			switch (board.getGrid().getGridType()) {
			case TRIANGLE:
				return new TriangleSchemaBoardPattern(board, f);
			case SQUARE:
				return new SquareSchemaBoardPattern(board, f);
			case DIAGONAL:
				return new DiagonalSchemaBoardPattern(board, f);
			case DIAGONALX:
				return new DiagonalXSchemaBoardPattern(board, f);
			}
		} catch (FileNotFoundException ex) {
			return AbstractBoardPattern.createBoardPattern(board,
					GridPattern.SIMPLE_EMPTY, (File) null);
		}
		throw new IllegalArgumentException();
	}

	public static BoardPattern createSchemaBoardPattern(Board board,
			Set<PointsEdge> pes) {
		switch (board.getGrid().getGridType()) {
		case TRIANGLE:
			return new TriangleSchemaBoardPattern(board, pes);
		case SQUARE:
			return new SquareSchemaBoardPattern(board, pes);
		case DIAGONAL:
			return new DiagonalSchemaBoardPattern(board, pes);
		case DIAGONALX:
			return new DiagonalXSchemaBoardPattern(board, pes);
		}
		throw new IllegalArgumentException();
	}

	private SchemaBoardPattern() {
	}

}
