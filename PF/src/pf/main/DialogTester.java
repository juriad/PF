package pf.main;

import java.util.ArrayList;
import java.util.List;

import pf.gui.PathPainterDialog;
import pf.interactive.PathPainterImpl;

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
		// new GridPainterDialog(null, true, null, false, null, true, null,
		// GridType.TRIANGLE).setVisible(true);
		List<PathPainterImpl> list = new ArrayList<PathPainterImpl>();
		list.add(new PathPainterImpl(null, null));
		new PathPainterDialog(null, list).setVisible(true);
	}

}
