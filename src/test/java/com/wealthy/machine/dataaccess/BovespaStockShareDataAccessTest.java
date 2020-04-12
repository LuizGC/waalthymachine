package com.wealthy.machine.dataaccess;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.share.BovespaDailyShare;
import com.wealthy.machine.share.DailyShare;
import com.wealthy.machine.sharecode.BovespaShareCode;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BovespaStockShareDataAccessTest {

	private static final String DAILY_SHARE_DATA = "DAILY_SHARE_DATA";

	private DailyShare createShareDataAccess(Date date, String shareCode){
		return new BovespaDailyShare(date, new BovespaShareCode(shareCode), "ABCD", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	@Test
	public void testSaveFolderConfiguration() throws IOException {
		var whereToSave = Files.createTempDirectory("test").toFile();
		var bovespaFolder = new File(whereToSave, StockExchange.BOVESPA.name());
		var bovespaShareDataAccess = new BovespaStockShareDataAccess(whereToSave);
		Calendar calendar = Calendar.getInstance();
		var shareListToSave = Set.of(
				createShareDataAccess(calendar.getTime(), "ABCD3"),
				createShareDataAccess(calendar.getTime(), "ABCD4")
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
	public void testListBovespaDaileShare() throws IOException {
		var whereToList = Files.createTempDirectory("test").toFile();
		var bovespaFolder = new File(whereToList, StockExchange.BOVESPA.name());
		Calendar calendar = Calendar.getInstance();
		var shareCode = "ABCD3";
		var firstDay = createShareDataAccess(calendar.getTime(), shareCode);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		var secondDay = createShareDataAccess(calendar.getTime(), shareCode);
		var shareCodeFolder = new File(bovespaFolder, shareCode);
		shareCodeFolder.mkdirs();
		var dailyShareRegisterFile = new File(shareCodeFolder, DAILY_SHARE_DATA);
		try(
				var fos = new FileOutputStream(dailyShareRegisterFile);
				var oos = new ObjectOutputStream(fos)
		) {
			oos.writeObject(Set.of(firstDay, secondDay));
		}
		var bovespaShareDataAccess = new BovespaStockShareDataAccess(whereToList);
		var arrayDaileShare = bovespaShareDataAccess.list(new BovespaShareCode(shareCode)).toArray();
		assertEquals(arrayDaileShare.length, 2);
		assertEquals(arrayDaileShare[0], firstDay);
		assertEquals(arrayDaileShare[1], secondDay);
	}

	@Test
	public void testIfsaveIsSavingCorrect() throws IOException {
		var whereToList = Files.createTempDirectory("test").toFile();
		Calendar calendar = Calendar.getInstance();
		var shareCodeText = "ABCD3";
		var firstDay = createShareDataAccess(calendar.getTime(), shareCodeText);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		var secondDay = createShareDataAccess(calendar.getTime(), shareCodeText);
		var bovespaShareDataAccess = new BovespaStockShareDataAccess(whereToList);
		bovespaShareDataAccess.save(Set.of(firstDay));
		var shareCode = new BovespaShareCode(shareCodeText);
		var arrayDaileShare = bovespaShareDataAccess.list(shareCode).toArray();
		assertEquals(arrayDaileShare[0], firstDay);
		bovespaShareDataAccess.save(Set.of(secondDay));
		arrayDaileShare = bovespaShareDataAccess.list(shareCode).toArray();
		assertEquals(arrayDaileShare[0], firstDay);
		assertEquals(arrayDaileShare[1], secondDay);
	}
}

