package com.wealthy.machine.dataaccesslayer.bovespa;

import com.wealthy.machine.quote.DailyQuote;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaYearManager {

	private static final Integer INITIAL_YEAR = 2000;

	private static final String YEAR_DOWNLOADED_FILE = "YEAR_DOWNLOADED_FILE";

	private final static String DEFAULT_URL = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";

	private final File yearDownloadedFile;

	public BovespaYearManager(File bovespaFolder) {
		this.yearDownloadedFile = new File(bovespaFolder, YEAR_DOWNLOADED_FILE);
	}

	public Collection<URL> listUnsavedPaths() {
		var savedYearSet = listSavedYears();
		return IntStream
				.range(INITIAL_YEAR, Year.now().getValue() + 1)
				.filter(year -> !savedYearSet.contains(year))
				.mapToObj(year -> DEFAULT_URL.replace("{{YYYY}}", String.valueOf(year)))
				.map(this::createUrl)
				.collect(Collectors.toUnmodifiableSet());
	}

	private Set<Integer> listSavedYears() {
		try (
				var fis = new FileInputStream(this.yearDownloadedFile);
				var ois = new ObjectInputStream(fis)
		){
			return Collections.unmodifiableSet((Set<Integer>) ois.readObject());
		} catch (Exception e) {
			return Collections.emptySet();
		}
	}

	public void updateDownloadedYear(Set<DailyQuote> dailyQuoteSet) {
		var newYearsSet = getNewYearsSet(dailyQuoteSet);
		var yearSet = new TreeSet<>(listSavedYears());
		yearSet.addAll(newYearsSet);
		try (
				var fos = new FileOutputStream(yearDownloadedFile);
				var oos = new ObjectOutputStream(fos)
		){
			oos.writeObject(yearSet);
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
