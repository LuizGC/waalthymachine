package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.BovespaShareCode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class BovespaDailyQuote implements DailyQuote {

    private final static DateFormat date_Format = new SimpleDateFormat("yyyyMMdd");

    // Used for internal purpose inside hash and equals class
    private final String tradingDayText;

    private final Date tradingDay;
    private final BovespaShareCode bovespaShareCode;
    private final String company;
    private final Double openPrice;
    private final Double closePrice;
    private final Double minPrice;
    private final Double maxPrice;
    private final Double avgPrice;
    private final Double volume;

    public BovespaDailyQuote(String tradingDay, BovespaShareCode bovespaShareCode, String company, Double openPrice, Double closePrice, Double minPrice, Double maxPrice, Double avgPrice, Double volume) throws ParseException {
        this.tradingDayText = tradingDay.trim();
        this.tradingDay = date_Format.parse(tradingDayText);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BovespaDailyQuote that = (BovespaDailyQuote) o;
        return getStockExchangeName().equals(that.getStockExchangeName()) &&
                tradingDayText.equals(that.tradingDayText) &&
                getShareCode().equals(that.getShareCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockExchangeName(), tradingDayText, getShareCode());
    }

}
