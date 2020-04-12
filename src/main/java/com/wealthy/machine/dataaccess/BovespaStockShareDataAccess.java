package com.wealthy.machine.dataaccess;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.quote.DailyShare;
import com.wealthy.machine.sharecode.ShareCode;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
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
	public synchronized void save(Set<DailyShare> dailyShareSet) {
		var dailyShareMap = dailyShareSet.stream().collect(Collectors.groupingBy(DailyShare::getShareCode));
		dailyShareMap.forEach(saveBovespaDaileSaheConsumer(dailyShareSet));
	}

	private BiConsumer<ShareCode, List<DailyShare>> saveBovespaDaileSaheConsumer(Set<DailyShare> dailyShareSet) {
		return (shareCode, list) -> {
			var dailyShareRegisterFile = getDailyShareRegisterFile(shareCode);
			var setToSave = new HashSet<DailyShare>(dailyShareSet);
			setToSave.addAll(list(shareCode));
			try (
					var fos = new FileOutputStream(dailyShareRegisterFile);
					var oos = new ObjectOutputStream(fos)
			){
				oos.writeObject(setToSave);
			} catch (Exception e) {
				throw new RuntimeException("There is an issue during saving the daily share set", e);
			}
		};
	}

	private File getDailyShareRegisterFile(ShareCode shareCode) {
		var shareFolder = new File(getBovespaFolder(), shareCode.getCode());
		shareFolder.mkdirs();
		return new File(shareFolder, DAILY_SHARE_DATA);
	}

	@Override
	public Set<DailyShare> list(ShareCode shareCode) {
		var dailyShareRegisterFile = getDailyShareRegisterFile(shareCode);
		try (
				var fis = new FileInputStream(dailyShareRegisterFile);
				var ois = new ObjectInputStream(fis)
		){
			var treeSet = new TreeSet<>(Comparator.comparing(DailyShare::getTradingDay));
			treeSet.addAll((Set<DailyShare>) ois.readObject());
			return Collections.unmodifiableSet(treeSet);
		} catch (Exception e) {
			return Collections.emptySet();
		}
	}
}
