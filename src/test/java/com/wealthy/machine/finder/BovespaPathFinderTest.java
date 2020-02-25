package com.wealthy.machine.finder;


import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BovespaPathFinderTest {

	@Test
	public void testUrlsToDownloadsList() {
		Set<String> correctYearsSet = new TreeSet<String>();
		String defaultUrl = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";
		Set<String> setToBeTested = new BovespaPathFinder().getPaths();
		for (int i = 1986; i <= Year.now().getValue(); i++) {
			String url = defaultUrl.replace("{{YYYY}}", String.valueOf(i));
			correctYearsSet.add(url);
		}
		assertEquals(correctYearsSet.size(), setToBeTested.size());
		assertTrue(correctYearsSet.containsAll(setToBeTested));
	}

}