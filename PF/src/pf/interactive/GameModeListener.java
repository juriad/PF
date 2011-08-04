package pf.interactive;

import java.util.EventListener;

public interface GameModeListener extends EventListener {
	public void modeEdit(GameModeEvent e);

	public void modeRun(GameModeEvent e);

	public void modeShow(GameModeEvent e);
}
