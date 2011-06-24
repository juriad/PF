package pf.board;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class ComplexBoardPattern extends AbstractBoardPattern {

	public static BoardPattern createComplexBoardPattern(Board board, File f,
			GridPattern gp) {
		switch (gp) {
		case COMPLEX_LIST:
			return ListBoardPattern.createListBoardPattern(board, f);
		case COMPLEX_SCHEMA:
			return SchemaBoardPattern.createSchemaBoardPattern(board, f);
		default:
			throw new IllegalArgumentException();
		}
	}

	protected ComplexBoardPattern(Board board, File f)
			throws FileNotFoundException {
		super();
		readFromFile(f);
	}

	protected abstract void readFromFile(File f) throws FileNotFoundException;
}
