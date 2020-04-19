package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.BovespaShareCode;
import com.wealthy.machine.util.BovespaDaileQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BovespaStockQuoteDataAccessLayerTest {

	private static final String DAILY_SHARE_DATA = "DAILY_SHARE_DATA";

	@Test
	public void testFolderConfiguration() throws IOException {
		var whereToSave = Files.createTempDirectory("testFolderConfiguration").toFile();
		var bovespaFolder = new File(whereToSave, StockExchange.BOVESPA.name());
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
		var whereToList = Files.createTempDirectory("testDataRegisteringRightOrder").toFile();
		Calendar calendar = Calendar.getInstance();
		var firstDay = new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		var secondDay = new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build();
		var bovespaShareDataAccessLayer = new BovespaStockQuoteDataAccessLayer(whereToList);
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
		var shareCodeTested = "SANB11";
		var setToBeSaved = Set.of(
				new BovespaDaileQuoteBuilder().shareCode(shareCodeTested).build(),
				new BovespaDaileQuoteBuilder().build()
		);
		var foder = Files.createTempDirectory("testTheDataRegistersInCorrectFile").toFile();
		var bovespaDataLayerAccess = new BovespaStockQuoteDataAccessLayer(foder);
		bovespaDataLayerAccess.save(setToBeSaved);
		var setWithData = bovespaDataLayerAccess.list(new BovespaShareCode(shareCodeTested));
		assertEquals(1, setWithData.size());
	}
}

