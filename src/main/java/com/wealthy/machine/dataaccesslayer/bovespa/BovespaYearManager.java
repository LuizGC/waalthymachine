package com.wealthy.machine.dataaccesslayer.bovespa;

import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.persistlayer.PersistLayer;
import com.wealthy.machine.quote.DailyQuote;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaYearManager {

	private final Integer initialYear;
	private final String defaultUrl;
	private final Logger logger;
	private final BovespaPersistLayerProxy proxy;

	public BovespaYearManager(BovespaPersistLayerProxy proxy) {
		var config = new Config();
		this.initialYear = config.getInitialYear();
		this.defaultUrl = config.getDefaultBovespaUrl();
		this.logger = config.getLogger(this.getClass());
		this.proxy = proxy;
	}

	public Set<URL> listUnsavedPaths() {
		var savedYearSet = listSavedYears();
		return IntStream
				.range(initialYear, Year.now().getValue() + 1)
				.filter(year -> !savedYearSet.contains(year))
				.mapToObj(year -> defaultUrl.replace("{{YYYY}}", String.valueOf(year)))
				.map(this::createUrl)
				.collect(Collectors.toUnmodifiableSet());
	}

	private Set<Integer> listSavedYears() {
		try {
			var quotesSet = this.proxy.readYear();
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			return Collections.emptySet();
		}
	}

	public void updateDownloadedYear(Set<DailyQuote> dailyQuoteSet) {
		var newYearsSet = getNewYearsSet(dailyQuoteSet);
		var yearSet = new HashSet<>(listSavedYears());
		yearSet.addAll(newYearsSet);
		try{
			this.proxy.saveYear(yearSet);
		} catch (Exception e) {
			logger.error("There is an issue during saving the daily share set", e);
			throw new RuntimeException(e);
		}
	}

	private Set<Integer> getNewYearsSet(Set<DailyQuote> dailyQuoteSet) {
		final var currentYear = Year.now().getValue();
		return dailyQuoteSet
				.stream()
				.map(quotes -> {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(quotes.getTradingDay());
					return calendar.get(Calendar.YEAR);
				})
				.filter(year -> currentYear != year)
				.collect(Collectors.toUnmodifiableSet());
	}

	private URL createUrl(String urlPath) {
		try {
			return new URL(urlPath);
		} catch (MalformedURLException e) {
			logger.error("Error during Bovespa Url creation.", e);
			throw new RuntimeException(e);
		}
	}
}
