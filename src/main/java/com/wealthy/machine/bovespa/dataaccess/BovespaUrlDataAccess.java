package com.wealthy.machine.bovespa.dataaccess;

import com.wealthy.machine.core.util.data.JsonDataFileHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Calendar.YEAR;

public class BovespaUrlDataAccess {

	private final int initialYear;
	private final String defaultUrl;
	private final JsonDataFileHandler jsonDataFileHandler;
	private final String downloadedUrlKey;

	public BovespaUrlDataAccess(JsonDataFileHandler jsonDataFileHandler, int initialYear, String defaultUrl) {
		this.initialYear = initialYear;
		this.defaultUrl = defaultUrl;
		this.jsonDataFileHandler = jsonDataFileHandler;
		this.downloadedUrlKey = "downloadedUrls";
	}

	public synchronized void save(Set<URL> newYearsSet) {
		var urlsText = newYearsSet.stream().map(URL::toString).collect(Collectors.toUnmodifiableSet());
		this.jsonDataFileHandler.append(this.downloadedUrlKey, urlsText, String.class);
	}

	public Set<URL> listMissingUrl() {
		var savedYearSet = this.jsonDataFileHandler.list(downloadedUrlKey, String.class);
		var calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		var lastUrlDownloaded = createUrl(calendar.get(YEAR));
		return IntStream
				.range(initialYear, Year.now().getValue() + 1)
				.mapToObj(this::createUrl)
				.filter(url -> lastUrlDownloaded.equals(url) || !savedYearSet.contains(url.toString()))
				.collect(Collectors.toUnmodifiableSet());
	}

	private URL createUrl(int year) {
		try {
			String urlPath = defaultUrl.replace("{{YYYY}}", String.valueOf(year));
			return new URL(urlPath);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

}
