package com.wealthy.machine.finder;


import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BovespaPathFinderTest {

	@Test
	public void testUrlsToDownloadsList() throws MalformedURLException {
		var setToBeTested = new BovespaPathFinder().getPaths();
		assertEquals(Year.now().getValue() - 1986, setToBeTested.size());
	}

}