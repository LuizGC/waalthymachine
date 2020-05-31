package com.wealthy.machine.bovespa.quote;

import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public final class BovespaDailyQuote implements DailyQuote,Comparable<BovespaDailyQuote>{

	private final Date tradingDay;
	private final BovespaShareCode bovespaShareCode;
	private final WealthNumber openPrice;
	private final WealthNumber closePrice;
	private final WealthNumber minPrice;
	private final WealthNumber maxPrice;
	private final WealthNumber avgPrice;
	private final WealthNumber volume;

	public BovespaDailyQuote(
			Date tradingDay,
			BovespaShareCode bovespaShareCode,
			WealthNumber openPrice,
			WealthNumber closePrice,
			WealthNumber minPrice,
			WealthNumber maxPrice,
			WealthNumber avgPrice,
			WealthNumber volume
	) {
		this.tradingDay = tradingDay;
		this.bovespaShareCode = bovespaShareCode;
		this.openPrice = openPrice;
		this.closePrice = closePrice;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.avgPrice = avgPrice;
		this.volume = volume;
	}

	@Override
	public Date getTradingDay() {
		return tradingDay;
	}

	@Override
	public BovespaShareCode getShareCode() {
		return new BovespaShareCode(bovespaShareCode.getCode());
	}

	@Override
	public WealthNumber getOpenPrice() {
		return openPrice;
	}

	@Override
	public WealthNumber getClosePrice() {
		return closePrice;
	}

	@Override
	public WealthNumber getMinPrice() {
		return minPrice;
	}

	@Override
	public WealthNumber getMaxPrice() {
		return maxPrice;
	}

	@Override
	public WealthNumber getAvgPrice() {
		return avgPrice;
	}

	@Override
	public WealthNumber getVolume() {
		return volume;
	}

	private String addLeadingZeroIfNecessary(int number) {
		return String.format("%02d",number);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BovespaDailyQuote that = (BovespaDailyQuote) o;
		return  getTextTradingDay().equals(that.getTextTradingDay()) &&
				getShareCode().equals(that.getShareCode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTextTradingDay(), getShareCode());
	}

	@Override
	public int compareTo(BovespaDailyQuote dailyQuote) {
		if (this.getShareCode().equals(dailyQuote.getShareCode())) {
			return this.getTextTradingDay().compareTo(dailyQuote.getTextTradingDay());
		}
		return this.getShareCode().compareTo(dailyQuote.getShareCode());
	}

	private String getTextTradingDay() {
		return getTextTradingDay(getTradingDay());
	}

	private String getTextTradingDay(Date tradingDay) {
		var cal = Calendar.getInstance();
		cal.setTime(tradingDay);
		var year = cal.get(Calendar.YEAR);
		var month = addLeadingZeroIfNecessary(cal.get(Calendar.MONTH));
		var day = addLeadingZeroIfNecessary(cal.get(Calendar.DAY_OF_MONTH));
		return "" + year + month +  day;
	}

}
