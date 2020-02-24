package com.wealthy.machine.finder;

import junit.framework.TestCase;
import org.junit.Test;

import java.time.Year;
import java.util.Set;
import java.util.TreeSet;

public class BovespaPathFinderTest extends TestCase {

	@Test
	public void testUrlsToDownloadsList() {
		Set<String> correctYearsSet = new TreeSet<String>();
		String defaultUrl = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";
		Set<String> setToBeTested = new BovespaPathFinder().getPaths();
		for (int i = 1986; i <= Year.now().getValue(); i++) {
			String url = defaultUrl.replace("{{YYYY}}", String.valueOf(i));
			correctYearsSet.add(url);
		}
		assertEquals("Test if the size of set is correct.", correctYearsSet.size(), setToBeTested.size());
		assertTrue("Test if the correct set contains all elements tested set.", correctYearsSet.containsAll(setToBeTested));
	}

}