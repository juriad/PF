package pf.animator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RandomizedIterator<E> implements Iterator<E> {
	private ArrayList<E> items;
	private int remaining;
	private Random random;

	public RandomizedIterator(Iterator<E> iterator) {
		items = new ArrayList<E>();
		while (iterator.hasNext()) {
			items.add(iterator.next());
		}
		remaining = items.size();
		random = new Random();
	}

	@Override
	public boolean hasNext() {
		return remaining > 0;
	}

	@Override
	public E next() {
		int index = random.nextInt(remaining);
		remaining--;
		E item = items.get(index);
		items.set(index, items.set(remaining, item));
		return item;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
