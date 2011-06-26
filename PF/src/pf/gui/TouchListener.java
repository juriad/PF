package pf.gui;

import java.util.EventListener;

public interface TouchListener extends EventListener {
	public void touchCancelled(TouchEvent e);

	public void touchEnded(TouchEvent e);

	public void touchLonger(TouchEvent e);

	public void touchShorter(TouchEvent e);

	public void touchStarted(TouchEvent e);
}
