package pf.board;

import java.io.File;
import java.io.FileNotFoundException;

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

	protected ComplexBoardPattern(Board board, File f)
			throws FileNotFoundException {
		super();
		readFromFile(f);
	}

	protected abstract void readFromFile(File f) throws FileNotFoundException;
}
