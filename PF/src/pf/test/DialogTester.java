package pf.test;

import pf.board.GridType;
import pf.gui.VerticesPainterDialog;

public class DialogTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// new NewDialog(null).setVisible(true);
		new VerticesPainterDialog(null, true, null, false, null, true, null,
				GridType.TRIANGLE).setVisible(true);
	}

}
