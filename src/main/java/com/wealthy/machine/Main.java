package com.wealthy.machine;

import com.wealthy.machine.dataaccesslayer.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.finder.BovespaPathFinder;
import com.wealthy.machine.finder.PathFinder;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		File file = new File(args[0]);
		file.mkdirs();
		run(new BovespaDataReader(), new BovespaPathFinder(), new BovespaStockQuoteDataAccessLayer(file));
	}

	public static void run (DataReader dataReader, PathFinder pathFinder, StockQuoteDataAccessLayer stockShareDataAccessLayer) {
		pathFinder
				.getPaths()
				.stream()
				.map(dataReader::read)
				.forEach(stockShareDataAccessLayer::save);
	}
}
