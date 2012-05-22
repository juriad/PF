package pf.animator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List of animators which are shown in {@link AnimatorDialog} and in
 * {@link NewDialog}. Each {@link Animator} must register its factory by
 * {@link #addAnimator(AnimatorFactory)}.
 * 
 * @author Adam Juraszek
 * 
 */
/**
 * @author Adam Juraszek
 * 
 */
public class Animators {
	private final List<AnimatorFactory> animators;

	private static volatile Animators instance = null;

	/**
	 * Singleton pattern with lazy initialization.
	 * 
	 * @return singleton instance
	 */
	public static Animators getInstance() {
		if (instance == null) {
			synchronized (Animators.class) {
				if (instance == null) {
					instance = new Animators();
				}
			}
		}
		return instance;
	}

	/**
	 * private to ensure singleton
	 */
	private Animators() {
		animators = new ArrayList<AnimatorFactory>();
	}

	/**
	 * Registers a new animator.
	 * 
	 * @param animator
	 */
	public void addAnimator(AnimatorFactory animator) {
		animators.add(animator);
	}

	/**
	 * @return list of all {@link AnimatorFactory}
	 */
	public List<AnimatorFactory> getAnimators() {
		return Collections.unmodifiableList(animators);
	}
}
