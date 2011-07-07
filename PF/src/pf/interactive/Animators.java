package pf.interactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Animators {
	private final List<AnimatorFactory> animators;

	private static volatile Animators instance = null;

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

	private Animators() {
		animators = new ArrayList<AnimatorFactory>();
	}

	public void addAnimtor(AnimatorFactory animator) {
		animators.add(animator);
	}

	public List<AnimatorFactory> getAnimators() {
		return Collections.unmodifiableList(animators);
	}
}
