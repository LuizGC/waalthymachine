package com.wealthy.machine.util;

import com.wealthy.machine.BigDecimalFactory;
import com.wealthy.machine.quote.DailyQuote;

import java.math.BigDecimal;
import java.util.Date;

public abstract class DailyQuoteBuilder {
	protected Date tradingDay;
	protected String shareCode;
	protected String company;
	protected BigDecimal openPrice;
	protected BigDecimal closePrice;
	protected BigDecimal minPrice;
	protected BigDecimal maxPrice;
	protected BigDecimal avgPrice;
	protected BigDecimal volume;

	public DailyQuoteBuilder() {
		this.tradingDay = new Date();
		this.shareCode = "ABCD3";
		this.company = "ABC";
		this.openPrice = new BigDecimalFactory().newInstance();
		this.closePrice = new BigDecimalFactory().newInstance();
		this.minPrice = new BigDecimalFactory().newInstance();
		this.maxPrice = new BigDecimalFactory().newInstance();
		this.avgPrice = new BigDecimalFactory().newInstance();
		this.volume = new BigDecimalFactory().newInstance();
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
		this.openPrice = new BigDecimalFactory(openPrice).newInstance();
		return this;
	}

	public DailyQuoteBuilder closePrice(Double closePrice) {
		this.closePrice =  new BigDecimalFactory(closePrice).newInstance();
		return this;
	}

	public DailyQuoteBuilder minPrice(Double minPrice) {
		this.minPrice =  new BigDecimalFactory(minPrice).newInstance();
		return this;
	}

	public DailyQuoteBuilder maxPrice(Double maxPrice) {
		this.maxPrice =  new BigDecimalFactory(maxPrice).newInstance();
		return this;
	}

	public DailyQuoteBuilder avgPrice(Double avgPrice) {
		this.avgPrice =  new BigDecimalFactory(avgPrice).newInstance();
		return this;
	}

	public DailyQuoteBuilder volume(Double volume) {
		this.volume = new BigDecimalFactory(volume).newInstance();
		return this;
	}

	public abstract DailyQuote build();

}
