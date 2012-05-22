package pf.animator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Buffers all items of original iterator and iterates over them in random
 * order.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of items
 */
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

	public List<E> getList() {
		return items;
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
