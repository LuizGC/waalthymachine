package com.wealthy.machine.util;

import com.wealthy.machine.quote.bovespa.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;

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
