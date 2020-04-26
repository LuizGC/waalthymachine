package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;
import com.wealthy.machine.util.BovespaDaileQuoteBuilder;
import com.wealthy.machine.util.UrlToYearConverter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static com.wealthy.machine.StockExchange.BOVESPA;
import static org.junit.jupiter.api.Assertions.*;

public class BovespaStockQuoteDataAccessLayerTest {

	private static final String DAILY_SHARE_DATA = "DAILY_SHARE_DATA";

	@Test
	public void testConstructorRules() {
		assertThrows(RuntimeException.class, () -> {
			var file = Files.createTempFile("test", "testConstructorRules").toFile();
			new BovespaStockQuoteDataAccessLayer(file);
		});
	}

	@Test
	public void testFolderConfiguration() throws IOException {
		var whereToSave = Files.createTempDirectory("testFolderConfiguration").toFile();
		var bovespaFolder = BOVESPA.getFolder(whereToSave);
		var bovespaShareDataAccess = new BovespaStockQuoteDataAccessLayer(whereToSave);
		Calendar calendar = Calendar.getInstance();
		var shareListToSave = Set.of(
				new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).shareCode("ABCD3").build(),
				new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).shareCode("ABCD4").build()
		);
		bovespaShareDataAccess.save(shareListToSave);
		shareListToSave.forEach(share -> {
			String shareCode = share.getShareCode().getCode();
			var folderShareContent = new File(bovespaFolder, shareCode);
			assertTrue(folderShareContent.exists());
			assertTrue(folderShareContent.isDirectory());
			var dataFile = new File(folderShareContent, DAILY_SHARE_DATA);
			assertTrue(dataFile.isFile());
			assertTrue(dataFile.length() > 0);
		});
	}

	@Test
	public void testDataRegisteringRightOrder() throws IOException {
		var whereToSave = Files.createTempDirectory("testDataRegisteringRightOrder").toFile();
		Calendar calendar = Calendar.getInstance();
		var firstDay = new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		var secondDay = new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build();
		var bovespaShareDataAccessLayer = new BovespaStockQuoteDataAccessLayer(whereToSave);
		bovespaShareDataAccessLayer.save(Set.of(firstDay));
		var shareCode = new BovespaShareCode(firstDay.getShareCode().getCode());
		var arrayDaileShare = bovespaShareDataAccessLayer.list(shareCode).toArray();
		assertEquals(arrayDaileShare[0], firstDay);
		bovespaShareDataAccessLayer.save(Set.of(secondDay));
		arrayDaileShare = bovespaShareDataAccessLayer.list(shareCode).toArray();
		assertEquals(arrayDaileShare[0], firstDay);
		assertEquals(arrayDaileShare[1], secondDay);
	}

	@Test
	public void testTheDataRegistersInCorrectFile() throws IOException {
		DailyQuote testedQuote = createBovespaDailyQuote();
		var calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		var setToBeSaved = Set.of(
				testedQuote,
				new BovespaDaileQuoteBuilder().tradingDay(new Date()).build(),
				new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build()
		);
		var folder = Files.createTempDirectory("testTheDataRegistersInCorrectFile").toFile();

		var bovespaDataLayerAccess = new BovespaStockQuoteDataAccessLayer(folder);
		bovespaDataLayerAccess.save(setToBeSaved);
		var setWithData = bovespaDataLayerAccess.list(testedQuote.getShareCode());

		assertFalse(setWithData.isEmpty());
		assertEquals(1, setWithData.size());
		assertTrue(setWithData.contains(testedQuote));
		var savedQuote = setWithData.stream().findFirst().orElseThrow();
		assertEquals(savedQuote.getShareCode(), testedQuote.getShareCode());
		assertEquals(savedQuote.getCompany(), testedQuote.getCompany());
		assertEquals(savedQuote.getOpenPrice(), testedQuote.getOpenPrice());
		assertEquals(savedQuote.getClosePrice(), testedQuote.getClosePrice());
		assertEquals(savedQuote.getMinPrice(), testedQuote.getMinPrice());
		assertEquals(savedQuote.getMaxPrice(), testedQuote.getMaxPrice());
		assertEquals(savedQuote.getAvgPrice(), testedQuote.getAvgPrice());
		assertEquals(savedQuote.getVolume(), testedQuote.getVolume());
	}

	private DailyQuote createBovespaDailyQuote() {
		return new BovespaDaileQuoteBuilder()
					.tradingDay(new Date())
					.shareCode("SANB11")
					.company("Santander")
					.openPrice(11.30)
					.closePrice(12.30)
					.minPrice(10.20)
					.maxPrice(13.00)
					.avgPrice(12.00)
					.volume(10000000)
					.build();
	}

	@Test
	public void testYearListReturningCorrectYear() throws IOException {
		var currentYear = Year.now().getValue();
		var whereToSave = Files.createTempDirectory("testYearListReturningCorrectYear").toFile();
		var bovespaShareDataAccessLayer = new BovespaStockQuoteDataAccessLayer(whereToSave);
		var pathList = bovespaShareDataAccessLayer.listUnsavedPaths();
		Set<Integer> savedYears =new UrlToYearConverter(pathList).listYears();
		assertTrue(savedYears.contains(currentYear), "Should contain the current year!");
		assertTrue(savedYears.contains(currentYear-1), "Should contain the previous year!");
		assertTrue(savedYears.contains(currentYear-2), "Should contain two year ago!");
	}
}

