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

	private final Integer initialYear;
	private final String defaultUrl;
	private final Logger logger;
	private final File yearFile;

	public BovespaYearManager(File bovespaFolder) {
		var config = new Config();
		this.initialYear = config.getInitialYear();
		this.defaultUrl = config.getDefaultBovespaUrl();
		this.logger = config.getLogger(this.getClass());
		if (!bovespaFolder.isDirectory()) {
			logger.error("The folder to persist the data must be a folder.");
			throw new RuntimeException();
		}
		try {
			var keyFolder = new File(bovespaFolder, "yearDownloadedFolder");
			keyFolder.mkdirs();
			this.yearFile = new File(keyFolder, config.getDefaultFilename());
			this.yearFile.createNewFile();
		} catch (IOException e) {
			logger.error("Error while creating year file", e);
			throw new RuntimeException(e);
		}
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
			var typeReference = new TypeReference<LinkedHashSet<Integer>>() {};
			var mapper = new ObjectMapper();
			var quotesSet = mapper.readValue(this.yearFile, typeReference);
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			return Collections.emptySet();
		}
	}

	public synchronized void updateDownloadedYears(Set<DailyQuote> dailyQuoteSet) {
		var newYearsSet = getNewYearsSet(dailyQuoteSet);
		var yearSet = new HashSet<>(listSavedYears());
		yearSet.addAll(newYearsSet);
		try{
			var mapper = new ObjectMapper();
			mapper.writeValue(this.yearFile, yearSet);
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
