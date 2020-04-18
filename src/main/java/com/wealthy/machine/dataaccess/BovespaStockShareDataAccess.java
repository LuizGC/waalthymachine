package com.wealthy.machine.dataaccess;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BovespaStockShareDataAccess implements StockShareDataAccess {

	private static final String DAILY_SHARE_DATA = "DAILY_SHARE_DATA";

	private final File whereIsData;

	public BovespaStockShareDataAccess(File whereIsData) {
		if (!whereIsData.isDirectory()) {
			throw new RuntimeException("The folder to persist the data must be a folder.");
		}
		if (!whereIsData.canWrite() || !whereIsData.canRead()) {
			throw new RuntimeException("The folder to persist the data must be readable and writable.");
		}
		this.whereIsData = whereIsData;
	}

	private File getBovespaFolder(){
		return new File(whereIsData, StockExchange.BOVESPA.name());
	}

	@Override
	public synchronized void save(Set<DailyQuote> dailyQuoteSet) {
		var dailyShareMap = dailyQuoteSet.stream().collect(Collectors.groupingBy(DailyQuote::getShareCode));
		dailyShareMap.forEach(saveBovespaDaileSaheConsumer(dailyQuoteSet));
	}

	private BiConsumer<ShareCode, List<DailyQuote>> saveBovespaDaileSaheConsumer(Set<DailyQuote> dailyQuoteSet) {
		return (shareCode, list) -> {
			var dailyQuoteRegisterFile = getDailyQuoteRegisterFile(shareCode);
			var setToSave = new HashSet<DailyQuote>(dailyQuoteSet);
			setToSave.addAll(list(shareCode));
			try (
					var fos = new FileOutputStream(dailyQuoteRegisterFile);
					var oos = new ObjectOutputStream(fos)
			){
				oos.writeObject(setToSave);
			} catch (Exception e) {
				throw new RuntimeException("There is an issue during saving the daily share set", e);
			}
		};
	}

	private File getDailyQuoteRegisterFile(ShareCode shareCode) {
		var shareFolder = new File(getBovespaFolder(), shareCode.getCode());
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
			var quotesSorted = new TreeSet<>(Comparator.comparing(DailyQuote::getTradingDay));
			quotesSorted.addAll((Set<DailyQuote>) ois.readObject());
			return Collections.unmodifiableSet(quotesSorted);
		} catch (Exception e) {
			return Collections.emptySet();
		}
	}
}
