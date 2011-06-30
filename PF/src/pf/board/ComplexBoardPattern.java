package pf.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

public abstract class ComplexBoardPattern extends AbstractBoardPattern {

	public static BoardPattern createComplexBoardPattern(Board b, File f,
			GridPattern gp) {
		switch (gp) {
		case COMPLEX_LIST:
			return ListBoardPattern.createListBoardPattern(b, f);
		case COMPLEX_SCHEMA:
			return SchemaBoardPattern.createSchemaBoardPattern(b, f);
		default:
			throw new IllegalArgumentException();
		}
	}

	public static BoardPattern createComplexBoardPattern(Board board,
			Set<PointsEdge> pes, GridPattern gp) {
		switch (gp) {
		case COMPLEX_LIST:
			return ListBoardPattern.createListBoardPattern(board, pes);
		case COMPLEX_SCHEMA:
			return SchemaBoardPattern.createSchemaBoardPattern(board, pes);
		default:
			throw new IllegalArgumentException();
		}
	}

	protected ComplexBoardPattern(Board board, File f)
			throws FileNotFoundException {
		super(board);
		readFromFile(f);
	}

	protected ComplexBoardPattern(Board board, Set<PointsEdge> pes) {
		super(board, pes);
	}

	protected abstract void readFromFile(File f) throws FileNotFoundException;
}
