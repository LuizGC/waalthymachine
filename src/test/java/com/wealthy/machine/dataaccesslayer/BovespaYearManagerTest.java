package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.dataaccesslayer.bovespa.BovespaYearManager;
import com.wealthy.machine.util.BovespaDaileQuoteBuilder;
import com.wealthy.machine.util.DailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Year;
import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wealthy.machine.StockExchange.BOVESPA;
import static org.junit.jupiter.api.Assertions.*;

public class BovespaYearManagerTest {

	private final static Integer CURRENT_YEAR = Year.now().getValue();

	private DailyQuoteBuilder createBovespaDailyQuote(Integer amountYear) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, CURRENT_YEAR);
		calendar.add(Calendar.YEAR, amountYear);
		return new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime());
	}

	@Test
	public void testUpdatingYearListSuccessfully() throws IOException {
		var yearManager = getBovespaYearManager();
		var quotesSave = Set.of(
			createBovespaDailyQuote(-2).build(),
			createBovespaDailyQuote(-1).build(),
			createBovespaDailyQuote(0).build()
		);
		yearManager.updateDownloadedYear(quotesSave);
		var savedYears = getYearList(yearManager);
		assertTrue(savedYears.contains(CURRENT_YEAR), "Should contain the current year!");
		assertFalse(savedYears.contains(CURRENT_YEAR-1), "Should not contain the previous year!");
		assertFalse(savedYears.contains(CURRENT_YEAR-2), "Should not contain two year ago!");
	}

	private Set<Integer> getYearList(BovespaYearManager yearManager) {
		return yearManager
					.listUnsavedPaths()
					.stream()
					.map(URL::toString)
					.map(urlText -> urlText.replaceAll("\\D", ""))
					.map(Integer::valueOf)
					.collect(Collectors.toUnmodifiableSet());
	}

	private BovespaYearManager getBovespaYearManager() throws IOException {
		var mainFolder = Files.createTempDirectory("testUpdatingYearListSuccessfully").toFile();
		var bovespaFolder = BOVESPA.getFolder(mainFolder);
		return new BovespaYearManager(bovespaFolder);
	}

	@Test
	public void testYearListReturningCorrectYear() throws IOException {
		var yearManager = getBovespaYearManager();
		Set<Integer> savedYears = getYearList(yearManager);
		assertTrue(savedYears.contains(CURRENT_YEAR), "Should contain the current year!");
		assertTrue(savedYears.contains(CURRENT_YEAR-1), "Should contain the previous year!");
		assertTrue(savedYears.contains(CURRENT_YEAR-2), "Should contain two year ago!");
	}

}
