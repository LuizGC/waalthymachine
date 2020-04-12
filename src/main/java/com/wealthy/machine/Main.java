package com.wealthy.machine;

import com.wealthy.machine.dataaccess.BovespaStockShareDataAccess;
import com.wealthy.machine.dataaccess.StockShareDataAccess;
import com.wealthy.machine.finder.BovespaPathFinder;
import com.wealthy.machine.finder.PathFinder;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		File file = new File(args[0]);
		file.mkdirs();
		run(new BovespaDataReader(), new BovespaPathFinder(), new BovespaStockShareDataAccess(file));
	}

	public static void run (DataReader dataReader, PathFinder pathFinder, StockShareDataAccess stockShareDataAccess) {
		pathFinder
				.getPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stockShareDataAccess::save);
	}
}
