package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.bovespa.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.TechnicalAnalysisCommander;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.List;

public class SimpleMovingAverage implements TechnicalAnalysisCommander {

	public WealthNumber calculate(final ValueType type, List<DailyQuote> dailyQuotes) {
		return dailyQuotes
				.stream()
				.map(type::getValue)
				.reduce(new WealthNumber("0"), WealthNumber::add)
				.divide(new WealthNumber(dailyQuotes.size()));
	}
}
