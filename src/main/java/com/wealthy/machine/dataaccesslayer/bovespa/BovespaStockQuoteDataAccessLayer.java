package com.wealthy.machine.dataaccesslayer.bovespa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.quote.bovespa.BovespaDailyQuote;
import com.wealthy.machine.quote.bovespa.BovespaDailyQuoteDeserializer;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.wealthy.machine.StockExchange.BOVESPA;

public class BovespaStockQuoteDataAccessLayer implements StockQuoteDataAccessLayer {

	private final Logger logger;
	private final BovespaYearManager yearManager;
	private final File bovespaFolder;
	private final String filename;

	public BovespaStockQuoteDataAccessLayer(File storageFolder) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.filename = config.getDefaultFilename();
		if (!storageFolder.isDirectory()) {
			logger.error("The folder to persist the data must be a folder.");
			throw new RuntimeException();
		}
		this.bovespaFolder = BOVESPA.getFolder(storageFolder);
		this.yearManager = new BovespaYearManager(this.bovespaFolder);
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
			var mapper = new ObjectMapper();
			mapper.writeValue(getFile(shareCode), setToSave);
		} catch (Exception e) {
			this.logger.error("There is an issue during saving the daily share set", e);
			throw new RuntimeException(e);
		}
	}

	private File getFile(ShareCode key) throws IOException {
		var keyFolder = new File(this.bovespaFolder, key.getCode());
		keyFolder.mkdirs();
		var file = new File(keyFolder, this.filename);
		file.createNewFile();
		return file;
	}

	@Override
	public Set<DailyQuote> list(ShareCode shareCode) {
		try {
			var typeReference = new TypeReference<LinkedHashSet<BovespaDailyQuote>>() {};
			var mapper = new ObjectMapper();
			var module = new SimpleModule();
			module.addDeserializer(BovespaDailyQuote.class, new BovespaDailyQuoteDeserializer((BovespaShareCode) shareCode));
			mapper.registerModule(module);
			var quotesSet = mapper.readValue(getFile(shareCode), typeReference);
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
