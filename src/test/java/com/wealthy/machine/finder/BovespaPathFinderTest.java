package com.wealthy.machine.finder;


import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.Year;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BovespaPathFinderTest {

	@Test
	public void testUrlsToDownloadsList() {
		var correctYearsSet = new TreeSet<String>();
		var defaultUrl = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";
		var setToBeTested = new BovespaPathFinder().getPaths();
		for (var i = 1986; i <= Year.now().getValue(); i++) {
			String url = defaultUrl.replace("{{YYYY}}", String.valueOf(i));
			correctYearsSet.add(url);
		}
		assertEquals(correctYearsSet.size(), setToBeTested.size());
		var containsAllCorrectURL = setToBeTested
				.stream()
				.map(URL::toString)
				.collect(Collectors.toUnmodifiableList())
				.containsAll(correctYearsSet);
		assertTrue(containsAllCorrectURL);
	}

}