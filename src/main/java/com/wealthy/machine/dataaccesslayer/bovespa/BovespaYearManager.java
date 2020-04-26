package com.wealthy.machine.dataaccesslayer.bovespa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.quote.DailyQuote;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaYearManager {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final TypeReference<LinkedHashSet<Integer>> TYPE_REFERENCE = new TypeReference<>() {};

	private static final Integer INITIAL_YEAR = 2000;

	private static final String YEAR_DOWNLOADED_FILE = "YEAR_DOWNLOADED_FILE";

	private final static String DEFAULT_URL = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";

	private final File yearDownloadedFile;

	public BovespaYearManager(File bovespaFolder) {
		this.yearDownloadedFile = new File(bovespaFolder, YEAR_DOWNLOADED_FILE);
	}

	public Set<URL> listUnsavedPaths() {
		var savedYearSet = listSavedYears();
		return IntStream
				.range(INITIAL_YEAR, Year.now().getValue() + 1)
				.filter(year -> !savedYearSet.contains(year))
				.mapToObj(year -> DEFAULT_URL.replace("{{YYYY}}", String.valueOf(year)))
				.map(this::createUrl)
				.collect(Collectors.toUnmodifiableSet());
	}

	private Set<Integer> listSavedYears() {
		try {
			var quotesSet = MAPPER.readValue(this.yearDownloadedFile, TYPE_REFERENCE);
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
			MAPPER.writeValue(this.yearDownloadedFile, yearSet);
		} catch (Exception e) {
			throw new RuntimeException("There is an issue during saving the daily share set", e);
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
			throw new RuntimeException(e);
		}
	}
}
