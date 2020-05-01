package com.wealthy.machine.dataaccesslayer.bovespa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.quote.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.wealthy.machine.StockExchange.BOVESPA;

public class BovespaStockQuoteDataAccessLayer implements StockQuoteDataAccessLayer {

	private static final TypeReference<LinkedHashSet<BovespaDailyQuote>> TYPE_REFERENCE = new TypeReference<>() {};

	private final String dailyShareDataFileName;
	private final ObjectMapper mapper;
	private final Logger logger;
	private final File bovespaFolder;
	private final BovespaYearManager yearManager;

	public BovespaStockQuoteDataAccessLayer(File whereIsData) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.mapper = config.getJsonMapper();
		this.dailyShareDataFileName = config.getDailyShareDataFileName();
		if (!whereIsData.isDirectory()) {
			this.logger.error("The folder to persist the data must be a folder.");
			throw new RuntimeException();
		}
		this.bovespaFolder = BOVESPA.getFolder(whereIsData);
		this.yearManager = new BovespaYearManager(this.bovespaFolder);
	}

	@Override
	public synchronized void save(Set<DailyQuote> dailyQuoteSet) {
		var dailyShareMap = dailyQuoteSet.stream().collect(Collectors.groupingBy(DailyQuote::getShareCode));
		dailyShareMap.forEach(this::saveBovespaDailyQuote);
		this.yearManager.updateDownloadedYear(dailyQuoteSet);
	}

	private void saveBovespaDailyQuote(ShareCode shareCode, List<DailyQuote> dailyQuoteSet) {
		var dailyQuoteRegisterFile = getDailyQuoteRegisterFile(shareCode);
		var setToSave = new TreeSet<>(list(shareCode));
		setToSave.addAll(dailyQuoteSet);
		try {
			mapper.writeValue(dailyQuoteRegisterFile, setToSave);
		} catch (Exception e) {
			this.logger.error("There is an issue during saving the daily share set", e);
			throw new RuntimeException(e);
		}
	}

	private File getDailyQuoteRegisterFile(ShareCode shareCode) {
		try {
			var shareFolder = new File(this.bovespaFolder, shareCode.getCode());
			shareFolder.mkdirs();
			var shareFile = new File(shareFolder, dailyShareDataFileName);
			shareFile.createNewFile();
			return shareFile;
		} catch (IOException e) {
			this.logger.error("Error during register file", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<DailyQuote> list(ShareCode shareCode) {
		var dailyShareRegisterFile = getDailyQuoteRegisterFile(shareCode);
		try {
			var quotesSet = mapper.readValue(dailyShareRegisterFile, TYPE_REFERENCE);
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
