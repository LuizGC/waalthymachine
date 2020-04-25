package com.wealthy.machine.util;

import com.wealthy.machine.quote.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;

import java.text.ParseException;

public class BovespaDaileQuoteBuilder extends DailyQuoteBuilder {

	public DailyQuote build() {
		try {
			return new BovespaDailyQuote(
					getTextTradingDay(),
					new BovespaShareCode(shareCode),
					company,
					openPrice,
					closePrice,
					minPrice,
					maxPrice,
					avgPrice,
					volume
			);
		} catch (ParseException e) {
			throw new RuntimeException("Date isn't formatted correctly", e);
		}
	}
}
