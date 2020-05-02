package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.dataaccesslayer.bovespa.BovespaPersistLayerProxy;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaYearManager;
import com.wealthy.machine.util.BovespaDaileQuoteBuilder;
import com.wealthy.machine.util.DailyQuoteBuilder;
import com.wealthy.machine.util.UrlToYearConverter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Year;
import java.util.Calendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		var savedYears = new UrlToYearConverter(yearManager.listUnsavedPaths()).listYears();
		assertTrue(savedYears.contains(CURRENT_YEAR), "Should contain the current year!");
		assertFalse(savedYears.contains(CURRENT_YEAR-1), "Should not contain the previous year!");
		assertFalse(savedYears.contains(CURRENT_YEAR-2), "Should not contain two year ago!");
	}



	private BovespaYearManager getBovespaYearManager() throws IOException {
		var mainFolder = Files.createTempDirectory("testUpdatingYearListSuccessfully").toFile();
		return new BovespaYearManager(new BovespaPersistLayerProxy(mainFolder));
	}

}
