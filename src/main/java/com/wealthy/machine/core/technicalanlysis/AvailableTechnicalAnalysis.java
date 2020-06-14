package com.wealthy.machine.core.technicalanlysis;


import com.wealthy.machine.bovespa.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.movingaverage.SimpleMovingAverage;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.List;

public enum AvailableTechnicalAnalysis {

	SIMPLE_MOVING_AVERAGE(new SimpleMovingAverage());

	private final TechnicalAnalysisCommander analysisCommander;

	AvailableTechnicalAnalysis(TechnicalAnalysisCommander simpleMovingAverage) {
		this.analysisCommander = simpleMovingAverage;
	}

	public WealthNumber calculate(ValueType type, List<DailyQuote> elements) {
		return this.analysisCommander.calculate(type, elements);
	}

}
