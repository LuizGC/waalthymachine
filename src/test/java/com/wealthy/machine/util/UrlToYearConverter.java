package com.wealthy.machine.util;

import java.net.URL;
import java.util.Set;
import java.util.stream.Collectors;

public class UrlToYearConverter {

	private final Set<URL> dailyQuotes;

	public UrlToYearConverter(Set<URL> dailyQuotes) {
		this.dailyQuotes = dailyQuotes;
	}

	public Set<Integer> listYears() {
		return this.dailyQuotes
				.stream()
				.map(URL::toString)
				.map(urlText -> urlText.replaceAll("\\D", ""))
				.map(Integer::valueOf)
				.collect(Collectors.toUnmodifiableSet());
	}
}
