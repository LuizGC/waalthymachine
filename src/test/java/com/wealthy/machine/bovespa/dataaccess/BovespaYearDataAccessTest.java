package com.wealthy.machine.core.dataaccess;

import com.wealthy.machine.bovespa.dataaccess.BovespaYearDataAccess;
import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import com.wealthy.machine.util.DailyQuoteBuilder;
import com.wealthy.machine.util.UrlToYearConverter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Year;
import java.util.Calendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BovespaYearDataAccessTest {

	private final static Integer CURRENT_YEAR = Year.now().getValue();

	private DailyQuoteBuilder createBovespaDailyQuote(Integer amountYear) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, CURRENT_YEAR);
		calendar.add(Calendar.YEAR, amountYear);
		return new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime());
	}

	@Test
	public void testUpdatingYearListSuccessfully() throws IOException {
		var mainFolder = Files.createTempDirectory("testUpdatingYearListSuccessfully").toFile();
		var yearManager =  new BovespaYearDataAccess(mainFolder);
		var quotesSave = Set.of(
			createBovespaDailyQuote(-2).build(),
			createBovespaDailyQuote(-1).build(),
			createBovespaDailyQuote(0).build()
		);
		yearManager.updateDownloadedYears(quotesSave);
		var savedYears = new UrlToYearConverter(yearManager.listUnsavedPaths()).listYears();
		assertTrue(savedYears.contains(CURRENT_YEAR), "Should contain the current year!");
		assertFalse(savedYears.contains(CURRENT_YEAR-1), "Should not contain the previous year!");
		assertFalse(savedYears.contains(CURRENT_YEAR-2), "Should not contain two year ago!");
	}

	@Test
	public void shouldThrowExceptionBecauseBovespaFolderIsFile() throws IOException {
		var file = Files.createTempFile("shouldThrowExceptionBecauseBovespaFolderIsFile", "test").toFile();
		assertThrows(RuntimeException.class, () -> new BovespaYearDataAccess(file));
	}

}
