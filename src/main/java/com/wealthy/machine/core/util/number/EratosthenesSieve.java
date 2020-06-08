package com.wealthy.machine.core.util.number;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class EratosthenesSieve {
	public Set<Integer> findPrimeNumber(int limit) {
		var primeNumbers = new TreeSet<Integer>();
		if (limit > 2) {
			var nonPrimeNumbers = new HashSet<Integer>();
			var i = 2;
			while (i < limit) {
				var j = i;
				if (!nonPrimeNumbers.contains(j)) {
					while (j < limit) {
						j += i;
						nonPrimeNumbers.add(j);
					}
					primeNumbers.add(i);
				}
				i++;
			}
		}
		return Collections.unmodifiableSet(primeNumbers);
	}
}