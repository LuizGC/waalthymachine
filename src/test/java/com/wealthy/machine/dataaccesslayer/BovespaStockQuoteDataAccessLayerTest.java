package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.math.number.WealthNumber;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;
import com.wealthy.machine.util.BovespaDailyQuoteBuilder;
import com.wealthy.machine.util.UrlToYearConverter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Year;
import java.util.*;

import static com.wealthy.machine.StockExchange.BOVESPA;
import static org.junit.jupiter.api.Assertions.*;

public class BovespaStockQuoteDataAccessLayerTest {

	private final String defaultFilename;

	public BovespaStockQuoteDataAccessLayerTest() {
		var config = new Config();
		this.defaultFilename = config.getDefaultFilename();
	}

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
				new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime()).shareCode("ABCD3").build(),
				new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime()).shareCode("ABCD4").build()
		);
		bovespaShareDataAccess.save(shareListToSave);
		shareListToSave.forEach(share -> {
			String shareCode = share.getShareCode().getCode();
			var folderShareContent = new File(bovespaFolder, shareCode);
			assertTrue(folderShareContent.exists());
			assertTrue(folderShareContent.isDirectory());
			var dataFile = new File(folderShareContent, defaultFilename);
			assertTrue(dataFile.isFile());
			assertTrue(dataFile.length() > 0);
		});
	}

	private DailyQuote[] createSetDiferentsDateQuotes(Integer amount){
		DailyQuote[] array = new DailyQuote[amount];
		Calendar calendar = Calendar.getInstance();
		for(int i = 0; i < amount; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + i);
			var quote = new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime()).build();
			array[i] = quote;
		}
		return array;
	}

	@Test
	public void testDataRegisteringRightOrder() throws IOException {
		var whereToSave = Files.createTempDirectory("testDataRegisteringRightOrder").toFile();
		var bovespaShareDataAccessLayer = new BovespaStockQuoteDataAccessLayer(whereToSave);
		for(var i = 1; i < 20; i++){
			var arrayQuotes = createSetDiferentsDateQuotes(i);
			var hashQuotes = new HashSet<>(Arrays.asList(arrayQuotes));
			bovespaShareDataAccessLayer.save(hashQuotes);
			var setSaved = bovespaShareDataAccessLayer.list(new BovespaShareCode("ABCD3"));
			assertFalse(setSaved.isEmpty());
			assertTrue(setSaved.containsAll(hashQuotes));
			assertEquals(arrayQuotes.length, setSaved.size());
			var arraySaved = setSaved.toArray();
			for(var j = 0; j < arraySaved.length; j++) {
				assertEquals(arrayQuotes[j], arraySaved[j]);
			}
		}
	}

	@Test
	public void testTheDataRegistersInCorrectFile() throws IOException {
		var tradingDay = new Date();
		var shareCode = "SANB11";
		var openPrice = "11.30";
		var closePrice = "12.30";
		var minPrice = "10.20";
		var maxPrice = "13.00";
		var avgPrice = "12.00";
		var volume = "10000000.00";
		DailyQuote testedQuote = new BovespaDailyQuoteBuilder()
				.tradingDay(tradingDay)
				.shareCode(shareCode)
				.openPrice(openPrice)
				.closePrice(closePrice)
				.minPrice(minPrice)
				.maxPrice(maxPrice)
				.avgPrice(avgPrice)
				.volume(volume)
				.build();;
		var calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		var setToBeSaved = Set.of(
				testedQuote,
				new BovespaDailyQuoteBuilder().tradingDay(new Date()).build(),
				new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime()).build()
		);
		var folder = Files.createTempDirectory("testTheDataRegistersInCorrectFile").toFile();

		var bovespaDataLayerAccess = new BovespaStockQuoteDataAccessLayer(folder);
		bovespaDataLayerAccess.save(setToBeSaved);
		var setWithData = bovespaDataLayerAccess.list(testedQuote.getShareCode());

		assertFalse(setWithData.isEmpty());
		assertEquals(1, setWithData.size());
		assertTrue(setWithData.contains(testedQuote));

		var savedQuote = setWithData.stream().findFirst().orElseThrow();
		assertEquals(tradingDay, savedQuote.getTradingDay());
		assertEquals(new BovespaShareCode(shareCode), savedQuote.getShareCode());
		assertEquals(new WealthNumber(openPrice), savedQuote.getOpenPrice());
		assertEquals(new WealthNumber(closePrice), savedQuote.getClosePrice());
		assertEquals(new WealthNumber(minPrice), savedQuote.getMinPrice());
		assertEquals(new WealthNumber(maxPrice), savedQuote.getMaxPrice());
		assertEquals(new WealthNumber(avgPrice), savedQuote.getAvgPrice());
		assertEquals(new WealthNumber(volume), savedQuote.getVolume());
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

