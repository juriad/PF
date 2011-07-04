package pf.interactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractAnimator implements Animator {
	private static List<AnimatorFactory> animators;

	static {
		animators = new ArrayList<AnimatorFactory>();
	}

	public static List<AnimatorFactory> getAnimators() {
		return Collections.unmodifiableList(animators);
	}

	protected static void addAnimtor(AnimatorFactory animator) {
		animators.add(animator);
	}
}
