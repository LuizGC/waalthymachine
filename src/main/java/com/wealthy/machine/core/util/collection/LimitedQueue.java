package com.wealthy.machine.core.util.collection;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LimitedQueue<E> {

	private final LinkedList<E> linkedList;
	private final int limit;

	public LimitedQueue(int limit) {
		this.linkedList = new LinkedList<>();
		this.limit = limit;
	}

	public boolean isCompletelyFilled() {
		return this.linkedList.size() == this.limit;
	}

	public void add(E e) {
		this.linkedList.add(e);
		while (this.linkedList.size() > this.limit) {
			this.linkedList.removeFirst();
		}
	}

	public List<E> sublist(int toIndex) {
		if (toIndex > this.linkedList.size()) {
			toIndex = this.linkedList.size();
		}
		if (toIndex < 0) {
			toIndex = 0;
		}
		var newList = this.linkedList.subList(0, toIndex);
		return Collections.unmodifiableList(newList);
	}
}
