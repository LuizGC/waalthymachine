package com.wealthy.machine.finder;

import java.time.Year;
import java.util.Set;
import java.util.TreeSet;

public class BovespaPathFinder implements PathFinder{

	private final static String DEFAULT_URL = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";

	@Override
	public Set<String> getPaths() {
		var urls = new TreeSet<String>();
		for (var i = 1986; i <= Year.now().getValue(); i++) {
			var url = DEFAULT_URL.replace("{{YYYY}}", String.valueOf(i));
			urls.add(url);
		}
		return urls;
	}
}
