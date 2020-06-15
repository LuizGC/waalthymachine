package com.wealthy.machine.bovespa.quote.type;

import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.function.Function;

public enum ValueType {

	OPEN(DailyQuote::getOpenPrice),
	CLOSE(DailyQuote::getClosePrice),
	LOW(DailyQuote::getLowPrice),
	HIGH(DailyQuote::getHighPrice);

	private final Function<DailyQuote, WealthNumber> valueGetter;

	ValueType(Function<DailyQuote, WealthNumber> valueGetter) {
		this.valueGetter = valueGetter;
	}

	public WealthNumber getValue(DailyQuote dailyQuote) {
		return valueGetter.apply(dailyQuote);
	}
}
