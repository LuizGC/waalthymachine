package com.wealthy.machine.dataaccesslayer.bovespa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wealthy.machine.dataaccesslayer.persistlayer.JsonPersistLayer;
import com.wealthy.machine.dataaccesslayer.persistlayer.PersistLayer;
import com.wealthy.machine.quote.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

import static com.wealthy.machine.StockExchange.BOVESPA;

public class BovespaPersistLayerProxy<quotesPersistLayerType> {

	private final PersistLayer<TreeSet<BovespaDailyQuote>> quotesPersistLayer;
	private final PersistLayer<HashSet<Integer>> yearPersistLayer;
	private final String yearKey;

	public BovespaPersistLayerProxy(PersistLayer<TreeSet<BovespaDailyQuote>> quotesPersistLayer, PersistLayer<HashSet<Integer>> yearPersistLayer) {
		this.quotesPersistLayer = quotesPersistLayer;
		this.yearPersistLayer = yearPersistLayer;
		this.yearKey = "downloadedYear";
	}

	public BovespaPersistLayerProxy(File storageFolder) {
		this(
				new JsonPersistLayer<>(BOVESPA.getFolder(storageFolder), new TypeReference<TreeSet<BovespaDailyQuote>>() {}) ,
				new JsonPersistLayer<>(BOVESPA.getFolder(storageFolder), new TypeReference<HashSet<Integer>>() {})
		);
	}

	public void saveYear(HashSet<Integer> years) throws IOException {
		this.yearPersistLayer.saveData(yearKey, years);
	}

	public void saveQuotes(ShareCode shareCode, TreeSet<DailyQuote> quotes) throws IOException {
		this.quotesPersistLayer.saveData(shareCode.getCode(), quotes);
	}

	public TreeSet<BovespaDailyQuote> readQuotes(ShareCode shareCode) throws IOException {
		return this.quotesPersistLayer.readFileContent(shareCode.getCode());
	}

	public HashSet<Integer> readYear() throws IOException {
		return this.yearPersistLayer.readFileContent(this.yearKey);
	}
}
