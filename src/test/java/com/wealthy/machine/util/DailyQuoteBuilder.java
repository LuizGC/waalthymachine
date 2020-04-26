package com.wealthy.machine.util;

import com.wealthy.machine.quote.DailyQuote;

import java.util.Date;

public abstract class DailyQuoteBuilder {
	protected Date tradingDay;
	protected String shareCode;
	protected String company;
	protected Double openPrice;
	protected Double closePrice;
	protected Double minPrice;
	protected Double maxPrice;
	protected Double avgPrice;
	protected Integer volume;

	public DailyQuoteBuilder() {
		this.tradingDay = new Date();
		this.shareCode = "ABCD3";
		this.company = "ABC";
		this.openPrice = 0.0;
		this.closePrice = 0.0;
		this.minPrice = 0.0;
		this.maxPrice = 0.0;
		this.avgPrice = 0.0;
		this.volume = 0;
	}

	public DailyQuoteBuilder tradingDay(Date tradingDay) {
		this.tradingDay = tradingDay;
		return this;
	}

	public DailyQuoteBuilder shareCode(String shareCode) {
		this.shareCode = shareCode;
		return this;
	}

	public DailyQuoteBuilder company(String company) {
		this.company = company;
		return this;
	}

	public DailyQuoteBuilder openPrice(Double openPrice) {
		this.openPrice = openPrice;
		return this;
	}

	public DailyQuoteBuilder closePrice(Double closePrice) {
		this.closePrice = closePrice;
		return this;
	}

	public DailyQuoteBuilder minPrice(Double minPrice) {
		this.minPrice = minPrice;
		return this;
	}

	public DailyQuoteBuilder maxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
		return this;
	}

	public DailyQuoteBuilder avgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
		return this;
	}

	public DailyQuoteBuilder volume(Integer volume) {
		this.volume = volume;
		return this;
	}

	public abstract DailyQuote build();

}
