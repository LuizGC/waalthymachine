package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.Config;
import com.wealthy.machine.core.dataaccess.StockQuoteDataAccessLayer;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuoteDeserializer;
import com.wealthy.machine.core.sharecode.ShareCode;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.wealthy.machine.core.StockExchange.BOVESPA;

public class BovespaStockQuoteDataAccessLayer implements StockQuoteDataAccessLayer {

	private final Logger logger;
	private final BovespaYearDataAccess yearManager;
	private final File bovespaFolder;
	private final String filename;
	private final BovespaShareCodeDataAccess shareCodeManger;
	private volatile Map<ShareCode, ReentrantLock> lockers;
	private final Executor executor;

	public BovespaStockQuoteDataAccessLayer(File storageFolder) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.filename = config.getDefaultFilename();
		if (!storageFolder.isDirectory()) {
			logger.error("The folder to persist the data must be a folder.");
			throw new RuntimeException();
		}
		this.bovespaFolder = BOVESPA.getFolder(storageFolder);
		this.yearManager = new BovespaYearDataAccess(this.bovespaFolder);
		this.executor = config.getDefaultExecutor();
		this.lockers = new ConcurrentHashMap<>();
		this.shareCodeManger = new BovespaShareCodeDataAccess(bovespaFolder);
	}

	@Override
	public void save(Set<DailyQuote> dailyQuoteSet) {
		try {
			var dailyShareMap = dailyQuoteSet.stream().collect(Collectors.groupingBy(DailyQuote::getShareCode));
			var latch = new CountDownLatch(dailyShareMap.size());
			synchronized (this) {
				dailyShareMap.keySet().forEach(shareCode -> {
					if (this.lockers.get(shareCode) == null) {
						this.lockers.put(shareCode, new ReentrantLock(false));
					}
				});
			}
			saveBovespaDailyQuote(dailyShareMap, latch);
			latch.await();
			this.yearManager.updateDownloadedYears(dailyQuoteSet);
			this.shareCodeManger.updateDownloadedShareCodes(dailyShareMap.keySet());
		} catch (Exception e) {
			this.logger.error("Error while processing the quote list", e);
			throw new RuntimeException(e);
		}
	}

	private void saveBovespaDailyQuote(Map<ShareCode, List<DailyQuote>> dailyShareMap, CountDownLatch latch) {
		dailyShareMap.forEach(((shareCode, dailyQuotes) -> {
			this.executor.execute(() -> {
				try {
					this.lockers.get(shareCode).lock();
					var setToSave = new TreeSet<>(list(shareCode));
					setToSave.addAll(dailyQuotes);
					var mapper = new ObjectMapper();
					mapper.writeValue(getFile(shareCode), setToSave);
					this.lockers.get(shareCode).unlock();
				} catch (Exception e) {
					this.logger.error("There is an issue during saving the daily share set", e);
					throw new RuntimeException(e);
				} finally {
					latch.countDown();
				}
			});
		}));
	}

	private File getFile(ShareCode shareCode) throws IOException {
		var keyFolder = new File(this.bovespaFolder, shareCode.getCode());
		keyFolder.mkdirs();
		var file = new File(keyFolder, this.filename);
		file.createNewFile();
		return file;
	}

	private Set<DailyQuote> list(ShareCode shareCode) {
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