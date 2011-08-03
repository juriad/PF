package pf.animator;

import pf.interactive.InteractiveBoard;

public interface AnimatorFactory {
	public Animator newInstance(InteractiveBoard board);
}
