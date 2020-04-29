package com.wealthy.machine.util;

import com.wealthy.machine.math.number.WealthNumber;
import com.wealthy.machine.quote.DailyQuote;

import java.util.Date;

public abstract class DailyQuoteBuilder {
	protected Date tradingDay;
	protected String shareCode;
	protected String company;
	protected WealthNumber openPrice;
	protected WealthNumber closePrice;
	protected WealthNumber minPrice;
	protected WealthNumber maxPrice;
	protected WealthNumber avgPrice;
	protected WealthNumber volume;

	public DailyQuoteBuilder() {
		this.tradingDay = new Date();
		this.shareCode = "ABCD3";
		this.company = "ABC";
		this.openPrice = new WealthNumber("0.0");
		this.closePrice = new WealthNumber("0.0");
		this.minPrice = new WealthNumber("0.0");
		this.maxPrice = new WealthNumber("0.0");
		this.avgPrice = new WealthNumber("0.0");
		this.volume = new WealthNumber("0.0");
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

	public DailyQuoteBuilder openPrice(String openPrice) {
		this.openPrice = new WealthNumber(openPrice);
		return this;
	}

	public DailyQuoteBuilder closePrice(String closePrice) {
		this.closePrice =  new WealthNumber(closePrice);
		return this;
	}

	public DailyQuoteBuilder minPrice(String minPrice) {
		this.minPrice =  new WealthNumber(minPrice);
		return this;
	}

	public DailyQuoteBuilder maxPrice(String maxPrice) {
		this.maxPrice =  new WealthNumber(maxPrice);
		return this;
	}

	public DailyQuoteBuilder avgPrice(String avgPrice) {
		this.avgPrice =  new WealthNumber(avgPrice);
		return this;
	}

	public DailyQuoteBuilder volume(String volume) {
		this.volume = new WealthNumber(volume);
		return this;
	}

	public abstract DailyQuote build();

}
