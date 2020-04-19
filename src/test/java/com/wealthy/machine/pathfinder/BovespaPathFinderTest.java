package com.wealthy.machine.pathfinder;


import org.junit.jupiter.api.Test;

import java.net.URL;
import java.time.Year;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BovespaPathFinderTest {

	private final static String DEFAULT_URL = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";

	@Test
	public void testUrlsToDownloadsList() {
		var setToBeTested = new BovespaPathFinder()
				.getPaths()
				.stream()
				.map(URL::toString)
				.collect(Collectors.toUnmodifiableList());
		for (var i = 1986; i <= Year.now().getValue(); i++) {
			var url = DEFAULT_URL.replace("{{YYYY}}", String.valueOf(i));
			assertTrue(setToBeTested.contains(url), "Such url has not been found " + url);
		}
	}

}