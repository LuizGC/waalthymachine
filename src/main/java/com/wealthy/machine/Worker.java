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

	public static void main(String... args) throws IOException {
		var path = args.length == 0 ? Files.createTempDirectory("storage_folder") : Path.of(args[0]);
		var storageFolder = path.toFile();
		var logFolder = new File(storageFolder, "log");
		System.setProperty("LOG_FILE", Paths.get(logFolder.toString(), "worker").toString());
		Logger logger = LoggerFactory.getLogger(Worker.class);
		logger.info("Worker started");
		run(new BovespaDataReader(), new BovespaStockQuoteDataAccessLayer(storageFolder));
		logger.info("storageFolder={}", storageFolder);
		logger.info("logFolder={}", logFolder);
		logger.info("Worker ended");
	}

	public static void run (DataReader dataReader, StockQuoteDataAccessLayer stockShareDataAccessLayer) {
		stockShareDataAccessLayer
				.listUnsavedPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stockShareDataAccessLayer::save);
	}
}
