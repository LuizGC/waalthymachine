package com.wealthy.machine;

import com.wealthy.machine.dataaccesslayer.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.dataaccesslayer.StockQuoteDataAccessLayer;
import com.wealthy.machine.pathfinder.BovespaPathFinder;
import com.wealthy.machine.pathfinder.PathFinder;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;

import java.io.File;

public class Worker {
	public static void main(String[] args) {
		File file = new File(args[0]);
		file.mkdirs();
		run(new BovespaPathFinder(), new BovespaDataReader(), new BovespaStockQuoteDataAccessLayer(file));
	}

	public static void run (PathFinder pathFinder, DataReader dataReader, StockQuoteDataAccessLayer stockShareDataAccessLayer) {
		pathFinder
				.getPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stockShareDataAccessLayer::save);
	}
}
