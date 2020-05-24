package com.wealthy.machine.bovespa.util;

import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Date;

public class BovespaDailyQuoteBuilder {

	private Date tradingDay;
	private String shareCode;
	private WealthNumber openPrice;
	private WealthNumber closePrice;
	private WealthNumber minPrice;
	private WealthNumber maxPrice;
	private WealthNumber avgPrice;
	private WealthNumber volume;

	public BovespaDailyQuoteBuilder() {
		this.tradingDay = new Date();
		this.shareCode = "ABCD3";
		this.openPrice = new WealthNumber("0.0");
		this.closePrice = new WealthNumber("0.0");
		this.minPrice = new WealthNumber("0.0");
		this.maxPrice = new WealthNumber("0.0");
		this.avgPrice = new WealthNumber("0.0");
		this.volume = new WealthNumber("0.0");
	}

	public BovespaDailyQuoteBuilder tradingDay(Date tradingDay) {
		this.tradingDay = tradingDay;
		return this;
	}

	public BovespaDailyQuoteBuilder shareCode(String shareCode) {
		this.shareCode = shareCode;
		return this;
	}

	public BovespaDailyQuoteBuilder openPrice(String openPrice) {
		this.openPrice = new WealthNumber(openPrice);
		return this;
	}

	public BovespaDailyQuoteBuilder closePrice(String closePrice) {
		this.closePrice =  new WealthNumber(closePrice);
		return this;
	}

	public BovespaDailyQuoteBuilder minPrice(String minPrice) {
		this.minPrice =  new WealthNumber(minPrice);
		return this;
	}

	public BovespaDailyQuoteBuilder maxPrice(String maxPrice) {
		this.maxPrice =  new WealthNumber(maxPrice);
		return this;
	}

	public BovespaDailyQuoteBuilder avgPrice(String avgPrice) {
		this.avgPrice =  new WealthNumber(avgPrice);
		return this;
	}

	public BovespaDailyQuoteBuilder volume(String volume) {
		this.volume = new WealthNumber(volume);
		return this;
	}

	public BovespaDailyQuote build() {
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
