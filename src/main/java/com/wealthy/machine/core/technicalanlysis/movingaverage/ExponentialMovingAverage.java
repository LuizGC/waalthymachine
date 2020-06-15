package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.TechnicalAnalysisCommander;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Collection;
import java.util.List;

public class ExponentialMovingAverage implements TechnicalAnalysisCommander {

	public WealthNumber calculate(final ValueType type, final Collection<DailyQuote> dailyQuotes) {
		var size = dailyQuotes.size();
		if (size == 0) {
			return WealthNumber.ZERO;
		}
		var iterator = dailyQuotes.iterator();
		var firstElement = iterator.next();
		if (size == 1) {
			return type.getValue(firstElement);
		}
		var secondElement = iterator.next();
		var simpleMovingAverage = new SimpleMovingAverage();
		var initialValue = simpleMovingAverage.calculate(type, List.of(firstElement, secondElement));
		return dailyQuotes
				.stream()
				.skip(2)
				.map(type::getValue)
				.reduce(initialValue, (yesterday, today) -> this.calculate(yesterday, today, size));

	}

	private WealthNumber calculate(WealthNumber yesterday, WealthNumber today, int size) {
		var smoothing = WealthNumber.TWO.divide(new WealthNumber(size));
		return today.multiply(smoothing).add(yesterday.multiply(WealthNumber.ONE.subtract(smoothing)));
	}
}
