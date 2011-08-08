package pf.animator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Buffers all items of original iterator and iterates over them in random
 * order.
 * 
 * @author Adam Juraszek
 * 
 * @param <E>
 *            type of items
 */
public class OrderedIterator<E> implements Iterator<E> {
	private ArrayList<E> items;
	private Iterator<E> itemsi;

	public OrderedIterator(Iterator<E> iterator, Comparator<E> comp) {
		items = new ArrayList<E>();
		while (iterator.hasNext()) {
			items.add(iterator.next());
		}

		Collections.sort(items, comp);

		itemsi = items.iterator();
	}

	public List<E> getList() {
		return items;
	}

	@Override
	public boolean hasNext() {
		return itemsi.hasNext();
	}

	@Override
	public E next() {
		return itemsi.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
