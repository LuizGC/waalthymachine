package com.wealthy.machine.dataaccesslayer.bovespa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;
import com.wealthy.machine.quote.DailyQuote;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaYearManager {

	private final ObjectMapper mapper;
	private final Integer initialYear;
	private final String defaultUrl;
	private final File yearDownloadedFile;
	private static final TypeReference<LinkedHashSet<Integer>> TYPE_REFERENCE = new TypeReference<>() {};

	private final Logger logger;

	public BovespaYearManager(File bovespaFolder) {
		var config = new Config();
		this.mapper = config.getJsonMapper();
		this.initialYear = config.getInitialYear();
		this.defaultUrl = config.getDefaultBovespaUrl();
		this.logger = config.getLogger(this.getClass());
		this.yearDownloadedFile = new File(bovespaFolder, config.getYearDownloadedBovespaFilename());
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
			var quotesSet = mapper.readValue(this.yearDownloadedFile, TYPE_REFERENCE);
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			return Collections.emptySet();
		}
	}

	public void updateDownloadedYear(Set<DailyQuote> dailyQuoteSet) {
		var newYearsSet = getNewYearsSet(dailyQuoteSet);
		var yearSet = new TreeSet<>(listSavedYears());
		yearSet.addAll(newYearsSet);
		try{
			mapper.writeValue(this.yearDownloadedFile, yearSet);
			logger.info("Completed Year={}", newYearsSet);
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
