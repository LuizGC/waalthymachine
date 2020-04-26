package com.wealthy.machine.util;

import com.wealthy.machine.quote.DailyQuote;

import java.math.BigDecimal;
import java.math.BigInteger;
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
	protected BigInteger volume;

	public DailyQuoteBuilder() {
		this.tradingDay = new Date();
		this.shareCode = "ABCD3";
		this.company = "ABC";
		this.openPrice = BigDecimal.ZERO;
		this.closePrice = BigDecimal.ZERO;
		this.minPrice = BigDecimal.ZERO;
		this.maxPrice = BigDecimal.ZERO;
		this.avgPrice = BigDecimal.ZERO;
		this.volume = BigInteger.ZERO;
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
		this.openPrice = BigDecimal.valueOf(openPrice);
		return this;
	}

	public DailyQuoteBuilder closePrice(Double closePrice) {
		this.closePrice =  BigDecimal.valueOf(closePrice);
		return this;
	}

	public DailyQuoteBuilder minPrice(Double minPrice) {
		this.minPrice =  BigDecimal.valueOf(minPrice);
		return this;
	}

	public DailyQuoteBuilder maxPrice(Double maxPrice) {
		this.maxPrice =  BigDecimal.valueOf(maxPrice);
		return this;
	}

	public DailyQuoteBuilder avgPrice(Double avgPrice) {
		this.avgPrice =  BigDecimal.valueOf(avgPrice);
		return this;
	}

	public DailyQuoteBuilder volume(Integer volume) {
		this.volume = BigInteger.valueOf(volume);
		return this;
	}

	public abstract DailyQuote build();

}
