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

	private final int totalDaysToProcess;
	private final SortedSet<Integer> primeNumbers;
	private final LimitedQueue<DailyQuote> queue;
	private final JsonDataFileHandler dataFileHandler;

	public BovespaTechnicalAnalysisData(int totalDaysToProcess, JsonDataFileHandler dataFileHandler, PrimeNumberFinder primeNumberFinder) {
		this.totalDaysToProcess = totalDaysToProcess;
		this.dataFileHandler = dataFileHandler;
		this.primeNumbers = primeNumberFinder
				.findPrimeNumber(totalDaysToProcess)
				.stream()
				.skip(4)
				.collect(Collectors.toCollection(TreeSet::new));
		this.queue = new LimitedQueue<>(primeNumbers.last());
	}

	public boolean createAnalysisFile(BovespaShareCode shareCode) {
		try {
			var module = new SimpleModule();
			module.addDeserializer(BovespaDailyQuote.class, new BovespaDailyQuoteDeserializer(shareCode));
			var list = dataFileHandler.list(shareCode.getCode(), BovespaDailyQuote.class, module);
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
			var hasAnalysisFile = matrix.size() > totalDaysToProcess;
			if (hasAnalysisFile) {
				dataFileHandler.override(shareCode.getCode() + "_ANALYSIS", matrix);
			}
			return hasAnalysisFile;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
