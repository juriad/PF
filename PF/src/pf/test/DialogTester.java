package pf.test;

import pf.board.GridType;
import pf.gui.GridPainterDialog;

public class DialogTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// new NewDialog(null).setVisible(true);
		// new VerticesPainterDialog(null, true, null, false, null, true, null,
		// GridType.TRIANGLE).setVisible(true);

		// new EdgesPainterDialog(null, true, null, false, null, true, null)
		// .setVisible(true);
		new GridPainterDialog(null, true, null, false, null, true, null,
				GridType.TRIANGLE).setVisible(true);
	}

}
