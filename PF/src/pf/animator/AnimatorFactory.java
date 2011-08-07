package pf.animator;

import pf.interactive.InteractiveBoard;

/**
 * Factory pattern to allow dynamically instantiate a new animator.
 * 
 * @author Adam Juraszek
 * 
 */
public interface AnimatorFactory {
	/**
	 * @param board
	 * @return new instance of animator
	 */
	Animator newInstance(InteractiveBoard board);
}
