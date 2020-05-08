package com.wealthy.machine.bovespa.processor;

import com.wealthy.machine.Config;
import com.wealthy.machine.bovespa.dataaccess.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.bovespa.seeker.BovespaDataSeeker;
import com.wealthy.machine.core.datamanager.DataManagerCommander;
import org.slf4j.Logger;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class BovespaDataProcessor implements DataManagerCommander {

	private final Logger logger;
	private final BovespaDataSeeker dataReader;
	private final BovespaStockQuoteDataAccessLayer stockShareDataAccessLayer;
	private final Executor executor;

	public BovespaDataProcessor(File storageFolder) {
		this(new BovespaDataSeeker(), new BovespaStockQuoteDataAccessLayer(storageFolder));
	}

	public BovespaDataProcessor(BovespaDataSeeker dataReader, BovespaStockQuoteDataAccessLayer stockShareDataAccessLayer) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.dataReader = dataReader;
		this.stockShareDataAccessLayer = stockShareDataAccessLayer;
		this.executor = config.getDefaultExecutor();
	}

	@Override
	public void getMissingData() {
		try {
			var paths = stockShareDataAccessLayer.listUnsavedPaths();
			var latch = new CountDownLatch(paths.size());
			logger.info("Bovespa data downloader started");
			paths.forEach(url -> this.executor.execute(() -> {
				try {
					var quotes = dataReader.read(url);
					stockShareDataAccessLayer.save(quotes);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					latch.countDown();
				}
			}));
			latch.await();
			logger.info("Bovespa data downloader ended");
		} catch (InterruptedException e) {
			logger.error("Long time waiting the download", e);
			throw new RuntimeException(e);
		}
	}
}

