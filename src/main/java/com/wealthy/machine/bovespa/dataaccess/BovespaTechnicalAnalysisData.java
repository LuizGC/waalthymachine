package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuoteDeserializer;
import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.AvailableTechnicalAnalysis;
import com.wealthy.machine.core.util.collection.ComparatorListAdapter;
import com.wealthy.machine.core.util.collection.LimitedQueue;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;
import com.wealthy.machine.core.util.number.WealthNumber;
import com.wealthy.machine.core.util.technicalanlysis.PrimeNumberFinder;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BovespaTechnicalAnalysisData {

	private final SortedSet<Integer> primeNumbers;
	private final JsonDataFileHandler dataFileHandler;
	private final int minimumAnalysed;

	public BovespaTechnicalAnalysisData(int totalDaysToProcess, int minimumAnalysed, JsonDataFileHandler dataFileHandler, PrimeNumberFinder primeNumberFinder) {
		this.minimumAnalysed = minimumAnalysed;
		this.dataFileHandler = dataFileHandler;
		this.primeNumbers = primeNumberFinder
				.findPrimeNumber(totalDaysToProcess)
				.stream()
				.skip(4)
				.collect(Collectors.toCollection(TreeSet::new));
	}

	public boolean createAnalysisFile(BovespaShareCode shareCode) {
		try {
			var queue = new LimitedQueue<DailyQuote>(primeNumbers.last());
			var module = new SimpleModule();
			module.addDeserializer(BovespaDailyQuote.class, new BovespaDailyQuoteDeserializer(shareCode));
			var list = dataFileHandler.list(shareCode.getCode(), BovespaDailyQuote.class, module);
			var hasAnalysisFile = list.size() > minimumAnalysed;
			if (hasAnalysisFile) {
				var matrix = new ArrayList<ComparatorListAdapter<WealthNumber>>();
				for (var dailyQuote : list) {
					queue.add(dailyQuote);
					if (queue.isCompletelyFilled()) {
						var line = new ArrayList<WealthNumber>();
						for (var valueType : ValueType.values()) {
							line.add(valueType.getValue(dailyQuote));
							for (var primeNumber : primeNumbers) {
								for (var analysis : AvailableTechnicalAnalysis.values()) {
									line.add(analysis.calculate(valueType, queue.sublist(primeNumber)));
								}
							}
						}
						matrix.add(new ComparatorListAdapter<>(line, matrix.size()));
					}
				}
				dataFileHandler.override(shareCode.getCode() + "_ANALYSIS", matrix);
			}
			return hasAnalysisFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
