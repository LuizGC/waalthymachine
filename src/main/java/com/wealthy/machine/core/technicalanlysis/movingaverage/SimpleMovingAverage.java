package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.TechnicalAnalysisCommander;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Collection;

public class SimpleMovingAverage implements TechnicalAnalysisCommander {

	public WealthNumber calculate(final ValueType type, final Collection<DailyQuote> dailyQuotes) {
		if (dailyQuotes.isEmpty()) {
			return WealthNumber.ZERO;
		} else {
			return dailyQuotes
					.stream()
					.map(type::getValue)
					.reduce(WealthNumber.ZERO, WealthNumber::add)
					.divide(new WealthNumber(dailyQuotes.size()));
		}
	}
}
