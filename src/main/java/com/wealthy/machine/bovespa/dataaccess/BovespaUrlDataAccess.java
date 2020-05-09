package com.wealthy.machine.bovespa.dataaccess;

import com.wealthy.machine.core.util.data.JsonDataFileHandler;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.dataaccess.DataAccess;
import com.wealthy.machine.core.seeker.MissingUrlSeeker;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaUrlDataAccess implements DataAccess<URL>, MissingUrlSeeker {

	private final Integer initialYear;
	private final String defaultUrl;
	private final Logger logger;
	private final JsonDataFileHandler jsonDataFileHandler;
	private final String downloadedUrlKey;

	public BovespaUrlDataAccess(JsonDataFileHandler jsonDataFileHandler, Config config) {
		this.initialYear = config.getInitialYear();
		this.defaultUrl = config.getDefaultBovespaUrl();
		this.logger = config.getLogger(this.getClass());
		this.downloadedUrlKey = config.getDownloadedUrlKey();
		this.jsonDataFileHandler = jsonDataFileHandler;
	}

	@Override
	public synchronized void save(Set<URL> newYearsSet) {
		this.jsonDataFileHandler.save(this.downloadedUrlKey, newYearsSet, URL.class);
	}

	@Override
	public Set<URL> listMissingUrl() {
		var savedYearSet = this.jsonDataFileHandler.list(downloadedUrlKey, URL.class);
		return IntStream
				.range(initialYear, Year.now().getValue() + 1)
				.mapToObj(this::createUrl)
				.filter(url -> !savedYearSet.contains(url))
				.collect(Collectors.toUnmodifiableSet());
	}

	private URL createUrl(Integer year) {
		try {
			String urlPath = defaultUrl.replace("{{YYYY}}", String.valueOf(year));
			return new URL(urlPath);
		} catch (MalformedURLException e) {
			logger.error("Error during Bovespa Url creation.", e);
			throw new RuntimeException(e);
		}
	}

}
