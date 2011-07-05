package pf.test;

import pf.gui.EdgesPainterDialog;

public class DialogTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// new NewDialog(null).setVisible(true);
		// new VerticesPainterDialog(null, true, null, false, null, true, null,
		// GridType.TRIANGLE).setVisible(true);

		new EdgesPainterDialog(null, true, null, false, null, true, null)
				.setVisible(true);
	}

}
