package com.wealthy.machine.bovespa.util;

import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.util.DailyQuoteBuilder;

public class BovespaDailyQuoteBuilder extends DailyQuoteBuilder {

	public DailyQuote build() {
		return new BovespaDailyQuote(
				tradingDay,
				new BovespaShareCode(shareCode),
				openPrice,
				closePrice,
				minPrice,
				maxPrice,
				avgPrice,
				volume
		);
	}
}
