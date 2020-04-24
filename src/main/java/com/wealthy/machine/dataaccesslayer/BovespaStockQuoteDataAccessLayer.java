package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.wealthy.machine.StockExchange.BOVESPA;

public class BovespaStockQuoteDataAccessLayer implements StockQuoteDataAccessLayer {

	private static final String DAILY_SHARE_DATA = "DAILY_SHARE_DATA";

	private final File bovespaFolder;

	public BovespaStockQuoteDataAccessLayer(File whereIsData) {
		if (!whereIsData.isDirectory()) {
			throw new RuntimeException("The folder to persist the data must be a folder.");
		}
		if (!whereIsData.canWrite() || !whereIsData.canRead()) {
			throw new RuntimeException("The folder to persist the data must be readable and writable.");
		}
		this.bovespaFolder = BOVESPA.getFolder(whereIsData);
	}

	@Override
	public synchronized void save(Set<DailyQuote> dailyQuoteSet) {
		var dailyShareMap = dailyQuoteSet.stream().collect(Collectors.groupingBy(DailyQuote::getShareCode));
		dailyShareMap.forEach(this::saveBovespaDaileSaheConsumer);
	}

	private void saveBovespaDaileSaheConsumer(ShareCode shareCode, List<DailyQuote> dailyQuoteSet) {
		var dailyQuoteRegisterFile = getDailyQuoteRegisterFile(shareCode);
		var setToSave = new TreeSet<>(list(shareCode));
		setToSave.addAll(dailyQuoteSet);
		try (
				var fos = new FileOutputStream(dailyQuoteRegisterFile);
				var oos = new ObjectOutputStream(fos)
		){
			oos.writeObject(setToSave);
		} catch (Exception e) {
			throw new RuntimeException("There is an issue during saving the daily share set", e);
		}

	}

	private File getDailyQuoteRegisterFile(ShareCode shareCode) {
		var shareFolder = new File(this.bovespaFolder, shareCode.getCode());
		shareFolder.mkdirs();
		return new File(shareFolder, DAILY_SHARE_DATA);
	}

	@Override
	public Set<DailyQuote> list(ShareCode shareCode) {
		var dailyShareRegisterFile = getDailyQuoteRegisterFile(shareCode);
		try (
				var fis = new FileInputStream(dailyShareRegisterFile);
				var ois = new ObjectInputStream(fis)
		){
			return Collections.unmodifiableSet((Set<DailyQuote>) ois.readObject());
		} catch (Exception e) {
			return Collections.emptySet();
		}
	}
}
