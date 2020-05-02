package com.wealthy.machine;

import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaPersistLayerProxy;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Worker {

	private final Logger logger;
	private final File storageFolder;

	private Worker(Path path) {
		this.storageFolder = path.toFile();
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		logger.info("Worker started");
		var bovespaPersistLayerProxy = new BovespaPersistLayerProxy(this.storageFolder);
		run(new BovespaDataReader(), new BovespaStockQuoteDataAccessLayer(bovespaPersistLayerProxy));
		logger.info("storageFolder={}", storageFolder);
		logger.info("Worker ended");
	}

	private void run (DataReader dataReader, StockQuoteDataAccessLayer stockShareDataAccessLayer) {
		stockShareDataAccessLayer
				.listUnsavedPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stockShareDataAccessLayer::save);
	}

	public static void main(String... args) throws IOException {
		var path = Files.createTempDirectory("storage_folder");
		System.setProperty("LOG_FOLDER", path.toString());
		new Worker(path);
	}

}
