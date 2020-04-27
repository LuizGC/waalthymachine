package com.wealthy.machine;

import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Worker {

	private final Logger logger;
	private final File storageFolder;
	private final File logFile;

	private Worker(Path path) {
		this.storageFolder = path.toFile();
		this.logFile = new File(storageFolder, "wealthy");
		System.setProperty("LOG_FILE", this.logFile.toString());
		this.logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Worker started");
		try {
			run(new BovespaDataReader(), new BovespaStockQuoteDataAccessLayer(storageFolder));
		} catch (Exception e) {
			logger.error("Worker doen't finish", e);
		}
		logger.info("storageFolder={}", storageFolder);
		logger.info("logFolder={}", logFile);
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
		var path = args.length == 0 ? Files.createTempDirectory("storage_folder") : Path.of(args[0]);
		new Worker(path);
	}

}
