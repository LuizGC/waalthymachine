package com.wealthy.machine.datamanager;

import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.reader.BovespaDataReader;
import org.slf4j.Logger;

import java.io.File;

public class BovespaDataManager implements DataManagerCommander {

	private final Logger logger;
	private final BovespaDataReader dataReader;
	private final BovespaStockQuoteDataAccessLayer stockShareDataAccessLayer;
	
	public BovespaDataManager(File storageFolder) {
		this(new BovespaDataReader(), new BovespaStockQuoteDataAccessLayer(storageFolder));
	}

	public BovespaDataManager(BovespaDataReader dataReader, BovespaStockQuoteDataAccessLayer stockShareDataAccessLayer) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.dataReader = dataReader;
		this.stockShareDataAccessLayer = stockShareDataAccessLayer;
	}

	@Override
	public void execute() {
		logger.info("Bovespa data downloader started");
		stockShareDataAccessLayer
				.listUnsavedPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stockShareDataAccessLayer::save);
		logger.info("Bovespa data downloader ended");
	}
}

