package com.wealthy.machine.dataaccesslayer.bovespa;

import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class BovespaStockQuoteDataAccessLayer implements StockQuoteDataAccessLayer {

	private final Logger logger;
	private final BovespaYearManager yearManager;
	private final BovespaPersistLayerProxy proxy;

	public BovespaStockQuoteDataAccessLayer(BovespaPersistLayerProxy proxy) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.proxy = proxy;
		this.yearManager = new BovespaYearManager(proxy);
	}

	@Override
	public synchronized void save(Set<DailyQuote> dailyQuoteSet) {
		var dailyShareMap = dailyQuoteSet.stream().collect(Collectors.groupingBy(DailyQuote::getShareCode));
		dailyShareMap.forEach(this::saveBovespaDailyQuote);
		this.yearManager.updateDownloadedYear(dailyQuoteSet);
	}

	private void saveBovespaDailyQuote(ShareCode shareCode, List<DailyQuote> dailyQuoteSet) {
		var setToSave = new TreeSet<>(list(shareCode));
		setToSave.addAll(dailyQuoteSet);
		try {
			proxy.saveQuotes(shareCode, setToSave);
		} catch (Exception e) {
			this.logger.error("There is an issue during saving the daily share set", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<DailyQuote> list(ShareCode shareCode) {
		try {
			var quotesSet = this.proxy.readQuotes(shareCode);
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			return Collections.emptySet();
		}
	}

	@Override
	public Set<URL> listUnsavedPaths() {
		return this.yearManager.listUnsavedPaths();
	}

}
