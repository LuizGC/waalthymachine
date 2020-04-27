package com.wealthy.machine.dataaccesslayer.bovespa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.quote.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wealthy.machine.StockExchange.BOVESPA;

public class BovespaStockQuoteDataAccessLayer implements StockQuoteDataAccessLayer {

	private static final String DAILY_SHARE_DATA = "DAILY_SHARE_DATA";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final TypeReference<LinkedHashSet<BovespaDailyQuote>> TYPE_REFERENCE = new TypeReference<>() {};

	private final Logger logger;
	private final File bovespaFolder;
	private final BovespaYearManager yearManager;

	public BovespaStockQuoteDataAccessLayer(File whereIsData) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		if (!whereIsData.isDirectory()) {
			throw new RuntimeException("The folder to persist the data must be a folder.");
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
			MAPPER.writeValue(dailyQuoteRegisterFile, setToSave);
			dailyQuoteSet.forEach(this::logSaveBovespaDailyQuote);
		} catch (Exception e) {
			throw new RuntimeException("There is an issue during saving the daily share set", e);
		}
	}

	private void logSaveBovespaDailyQuote(DailyQuote quote) {
		var format = new SimpleDateFormat("dd/MM/yyyy");
		logger.info("shareCode={} date={}", quote.getShareCode().getCode(), format.format(quote.getTradingDay()));
	}

	private File getDailyQuoteRegisterFile(ShareCode shareCode) {
		try {
			var shareFolder = new File(this.bovespaFolder, shareCode.getCode());
			shareFolder.mkdirs();
			var shareFile = new File(shareFolder, DAILY_SHARE_DATA);
			shareFile.createNewFile();
			return shareFile;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<DailyQuote> list(ShareCode shareCode) {
		var dailyShareRegisterFile = getDailyQuoteRegisterFile(shareCode);
		try {
			var quotesSet = MAPPER.readValue(dailyShareRegisterFile, TYPE_REFERENCE);
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
