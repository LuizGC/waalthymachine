package com.wealthy.machine.core.technicalanlysis.bollingerband;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.technicalanlysis.TechnicalAnalysisCommander;
import com.wealthy.machine.core.technicalanlysis.movingaverage.SimpleMovingAverage;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Collection;

public class BollingerBand implements TechnicalAnalysisCommander {

	private final SimpleMovingAverage simpleMovingAverage;
	private final WealthNumber factor;
	private final BollingerBandSide bandSide;

	public BollingerBand(BollingerBandSide bandSide, int factor) {
		this.simpleMovingAverage = new SimpleMovingAverage();
		this.factor = new WealthNumber(factor);
		this.bandSide = bandSide;
	}

	public WealthNumber calculate(ValueType type, Collection<DailyQuote> quotes) {
		if (quotes.isEmpty()) {
			return WealthNumber.ZERO;
		}
		var average = simpleMovingAverage.calculate(type, quotes);
		var standardDeviation = quotes
				.stream()
				.map(type::getValue)
				.map(average::subtract)
				.map((a) -> a.multiply(a))
				.reduce(WealthNumber.ZERO, WealthNumber::add)
				.divide(new WealthNumber(quotes.size()))
				.sqrt();
		return this.bandSide.calculate(average, standardDeviation.multiply(this.factor));
	}

}
