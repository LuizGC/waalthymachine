package com.wealthy.machine.core.util.collection;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collection;
import java.util.Collections;

public class ComparatorListAdapter<T> implements Comparable<ComparatorListAdapter<T>> {

	private final Collection<T> collection;
	private final Integer position;

	public ComparatorListAdapter(Collection<T> collection, Integer position) {
		this.collection = collection;
		this.position = position;
	}

	@Override
	public int compareTo(ComparatorListAdapter<T> otherPosition) {
		return this.position.compareTo(otherPosition.position);
	}

	@JsonValue
	public Collection<T> getCollection() {
		return Collections.unmodifiableCollection(collection);
	}
}
