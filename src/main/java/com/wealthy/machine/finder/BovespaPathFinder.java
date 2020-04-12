package com.wealthy.machine.finder;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class BovespaPathFinder implements PathFinder{

	private final static String DEFAULT_URL = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";

	@Override
	public Set<URL> getPaths() {
		try {
			var urls = new HashSet<URL>();
			for (var i = 1986; i <= Year.now().getValue(); i++) {
				var url = DEFAULT_URL.replace("{{YYYY}}", String.valueOf(i));
				urls.add(new URL(url));
			}
			return urls;
		} catch (MalformedURLException e) {
			throw new RuntimeException("URL to access the shares data is invelid.", e);
		}
	}
}
