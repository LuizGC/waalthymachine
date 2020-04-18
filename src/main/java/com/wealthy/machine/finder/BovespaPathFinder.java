package com.wealthy.machine.finder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaPathFinder implements PathFinder{

	private final static String DEFAULT_URL = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";

	@Override
	public Set<URL> getPaths() {
		return IntStream
				.range(1986, Year.now().getValue())
				.mapToObj(this::createDownloadPath)
				.map(this::createPath)
				.collect(Collectors.toUnmodifiableSet());
	}

	private String createDownloadPath(int year) {
		return DEFAULT_URL.replace("{{YYYY}}", String.valueOf(year));
	}

	private URL createPath(String urlPath) {
		try {
			return new URL(urlPath);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
