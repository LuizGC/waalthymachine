package com.wealthy.machine;

import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;

import java.io.File;

public class Worker {
	public static void main(String[] args) {
		File file = new File(args[0]);
		file.mkdirs();
		run(new BovespaDataReader(), new BovespaStockQuoteDataAccessLayer(file));
	}

	public static void run (DataReader dataReader, StockQuoteDataAccessLayer stockShareDataAccessLayer) {
		stockShareDataAccessLayer
				.listUnsavedPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stockShareDataAccessLayer::save);
	}
}
