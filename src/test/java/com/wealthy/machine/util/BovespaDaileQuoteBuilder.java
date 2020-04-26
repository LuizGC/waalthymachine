package com.wealthy.machine.util;

import com.wealthy.machine.quote.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;

public class BovespaDaileQuoteBuilder extends DailyQuoteBuilder {

	public DailyQuote build() {
		return new BovespaDailyQuote(
				tradingDay,
				new BovespaShareCode(shareCode),
				company,
				openPrice,
				closePrice,
				minPrice,
				maxPrice,
				avgPrice,
				volume
		);
	}
}
