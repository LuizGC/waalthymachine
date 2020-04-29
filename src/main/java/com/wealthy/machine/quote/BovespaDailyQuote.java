package com.wealthy.machine.quote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wealthy.machine.StockExchange;
import com.wealthy.machine.math.number.WealthNumber;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;


import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public final class BovespaDailyQuote implements DailyQuote {

    private final Date tradingDay;
    private final BovespaShareCode bovespaShareCode;
    private final String company;
    private final WealthNumber openPrice;
    private final WealthNumber closePrice;
    private final WealthNumber minPrice;
    private final WealthNumber maxPrice;
    private final WealthNumber avgPrice;
    private final WealthNumber volume;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BovespaDailyQuote(
            @JsonProperty("tradingDay") Date tradingDay,
            @JsonProperty("shareCode") BovespaShareCode bovespaShareCode,
            @JsonProperty("company") String company,
            @JsonProperty("openPrice") WealthNumber openPrice,
            @JsonProperty("closePrice") WealthNumber closePrice,
            @JsonProperty("minPrice") WealthNumber minPrice,
            @JsonProperty("maxPrice") WealthNumber maxPrice,
            @JsonProperty("avgPrice") WealthNumber avgPrice,
            @JsonProperty("volume") WealthNumber volume
    ) {
        this.tradingDay = tradingDay;
        this.bovespaShareCode = bovespaShareCode;
        this.company = company;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.avgPrice = avgPrice;
        this.volume = volume;
    }

    @JsonIgnore
    public StockExchange getStockExchangeName() {
        return StockExchange.BOVESPA;
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
    public String getCompany() {
        return company;
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

    private String getTextTradingDay() {
        var cal = Calendar.getInstance();
        cal.setTime(getTradingDay());
        var year = cal.get(Calendar.YEAR);
        var month = cal.get(Calendar.MONTH);
        var day = cal.get(Calendar.DAY_OF_MONTH);
        return "" + year + month +  day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BovespaDailyQuote that = (BovespaDailyQuote) o;
        return getStockExchangeName().equals(that.getStockExchangeName()) &&
                getTextTradingDay().equals(that.getTextTradingDay()) &&
                getShareCode().equals(that.getShareCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockExchangeName(), getTextTradingDay(), getShareCode());
    }

    @Override
    public int compareTo(Object o) {
        var dailyQuote = (BovespaDailyQuote)o;
        if (!this.getShareCode().equals(dailyQuote.getShareCode())) {
            throw new RuntimeException("Share code must be the same");
        }
        if (!this.getStockExchangeName().equals(dailyQuote.getStockExchangeName())) {
            throw new RuntimeException("Stock exchange must be the same");
        }
        return getTradingDay().compareTo(dailyQuote.getTradingDay());
    }


}
