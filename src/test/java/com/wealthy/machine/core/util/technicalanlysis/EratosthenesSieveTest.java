package com.wealthy.machine.core.util.technicalanlysis;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EratosthenesSieveTest {

	private final EratosthenesSieve primeNumberSeeker;

	public EratosthenesSieveTest() {
		this.primeNumberSeeker = new EratosthenesSieve();
	}

	@Test
	public void findPrimeNumber_WhenLimitIsTen_ShouldReturnSetWithCorrectPrimeNumbers() {
		Set<Integer> expected = new TreeSet<>(Set.of(2, 3, 5, 7));
		Set<Integer> primeNumbers = this.primeNumberSeeker.findPrimeNumber(10);
		assertIterableEquals(expected, primeNumbers);
	}

	@Test
	public void findPrimeNumber_WhenLimitIsLessThanTwo_ShouldReturnEmptyList() {
		Set<Integer> primeNumbers = this.primeNumberSeeker.findPrimeNumber(1);
		assertTrue(primeNumbers.isEmpty());
	}

	@Test
	public void findPrimeNumber_WhenLimitIsSeven_ShouldNotReturnSeven() {
		Set<Integer> expected = new TreeSet<>(Set.of(2, 3, 5));
		Set<Integer> primeNumbers = this.primeNumberSeeker.findPrimeNumber(7);
		assertIterableEquals(expected, primeNumbers);
	}

}