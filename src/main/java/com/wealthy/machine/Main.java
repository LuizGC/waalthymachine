package com.wealthy.machine;

import com.wealthy.machine.finder.BovespaPathFinder;
import com.wealthy.machine.finder.PathFinder;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.reader.DataReader;

public class Main {
	public static void main(String[] args) {
		run(new BovespaDataReader(), new BovespaPathFinder());
	}

	public static void run (DataReader dataReader, PathFinder pathFinder) {
		pathFinder
				.getPaths()
				.parallelStream()
				.map(dataReader::read)
				.forEach(stream -> stream.forEach(System.out::println));
	}
}
