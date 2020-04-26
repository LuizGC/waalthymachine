package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public final class BovespaDailyQuote implements DailyQuote {



    private final Date tradingDay;
    private final BovespaShareCode bovespaShareCode;
    private final String company;
    private final Double openPrice;
    private final Double closePrice;
    private final Double minPrice;
    private final Double maxPrice;
    private final Double avgPrice;
    private final Double volume;

    public BovespaDailyQuote(Date tradingDay, BovespaShareCode bovespaShareCode, String company, Double openPrice, Double closePrice, Double minPrice, Double maxPrice, Double avgPrice, Double volume) {
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
    public Double getOpenPrice() {
        return openPrice;
    }

    @Override
    public Double getClosePrice() {
        return closePrice;
    }

    @Override
    public Double getMinPrice() {
        return minPrice;
    }

    @Override
    public Double getMaxPrice() {
        return maxPrice;
    }

    @Override
    public Double getAvgPrice() {
        return avgPrice;
    }

    @Override
    public Double getVolume() {
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
