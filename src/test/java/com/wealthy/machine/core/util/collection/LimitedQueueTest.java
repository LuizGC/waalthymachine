package com.wealthy.machine.core.util.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LimitedQueueTest {

	@Test
	void isCompletelyFilled_WhenQueueIsNotCompletelyFilled_ShouldReturnFalse() {
		var queue = new LimitedQueue<Integer>(5);
		queue.add(1);
		assertFalse(queue.isCompletelyFilled());
	}

	@Test
	void isCompletelyFilled_WhenQueueIsCompletelyFilled_ShouldReturnTrue() {
		var queue = new LimitedQueue<Integer>(2);
		queue.add(1);
		queue.add(2);
		assertTrue(queue.isCompletelyFilled());
	}

	@Test
	void sublist_WhenToIndexIsBiggerThanQueueSize_ShouldReturnWholeQueue() {
		var queue = new LimitedQueue<Integer>(2);
		queue.add(1);
		queue.add(2);
		assertIterableEquals(List.of(1, 2), queue.sublist(100));
	}

	@Test
	void sublist_WhenToIndexIsLessOrEqualZero_ShouldReturnEmptyList() {
		var queue = new LimitedQueue<Integer>(2);
		queue.add(1);
		queue.add(2);
		assertTrue(queue.sublist(0).isEmpty());
		assertTrue(queue.sublist(-1).isEmpty());
	}

	@Test
	void sublist_WhenQueueIsFullyFilledAndToIndexIsEqualThree_ShouldReturnListWithThreeElements() {
		var queue = new LimitedQueue<Integer>(4);
		queue.add(1);
		queue.add(2);
		queue.add(3);
		queue.add(4);
		assertIterableEquals(List.of(1, 2, 3), queue.sublist(3));
	}

	@Test
	void sublist_WhenQueueIsFullyFilled_ShouldReturnListWithNewerElements() {
		var queue = new LimitedQueue<Integer>(2);
		queue.add(1);
		queue.add(2);
		queue.add(20);
		queue.add(10);
		assertIterableEquals(List.of(20, 10), queue.sublist(2));
	}
}