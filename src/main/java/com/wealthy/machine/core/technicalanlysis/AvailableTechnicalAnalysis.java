package com.wealthy.machine.core.technicalanlysis;


import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.bollingerband.BollingerBand;
import com.wealthy.machine.core.technicalanlysis.bollingerband.BollingerBandSide;
import com.wealthy.machine.core.technicalanlysis.movingaverage.ExponentialMovingAverage;
import com.wealthy.machine.core.technicalanlysis.movingaverage.SimpleMovingAverage;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.List;

public enum AvailableTechnicalAnalysis {

	SIMPLE_MOVING_AVERAGE(new SimpleMovingAverage()),
	EXPONENTIAL_MOVING_AVERAGE(new ExponentialMovingAverage()),
	UPPER_BOLLINGER_BAND(new BollingerBand(BollingerBandSide.UPPER, 2)),
	LOWER_BOLLINGER_BAND(new BollingerBand(BollingerBandSide.LOWER, 2));

	private final TechnicalAnalysisCommander analysisCommander;

	AvailableTechnicalAnalysis(TechnicalAnalysisCommander simpleMovingAverage) {
		this.analysisCommander = simpleMovingAverage;
	}

	public WealthNumber calculate(ValueType type, List<DailyQuote> elements) {
		return this.analysisCommander.calculate(type, elements);
	}

}
