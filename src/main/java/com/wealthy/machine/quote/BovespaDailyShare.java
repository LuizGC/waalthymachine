package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.BovespaShareCode;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public final class BovespaDailyShare implements DailyShare {

    //List with codes of possible cash market stocks: 3 is ON, 4 is PN or 11 UNIT
    //It must works for fractional share market: 3F, 4F, 11F
    private final static Set<String> CODES_ALLOWED_CASH_MARKET = Set.of("3", "3F", "4", "4F", "11", "11F");

    private final Date tradingDay;
    private final BovespaShareCode bovespaShareCode;
    private final String company;
    private final Double openPrice;
    private final Double closePrice;
    private final Double minPrice;
    private final Double maxPrice;
    private final Double avgPrice;
    private final Double volume;

    public BovespaDailyShare(Date tradingDay, BovespaShareCode bovespaShareCode, String company, Double openPrice, Double closePrice, Double minPrice, Double maxPrice, Double avgPrice, Double volume) {
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

    private String getTradingDayFormated() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(tradingDay);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year + "" + month + "" + day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BovespaDailyShare that = (BovespaDailyShare) o;
        return getStockExchangeName().equals(that.getStockExchangeName()) &&
                getTradingDayFormated().equals(that.getTradingDayFormated()) &&
                getShareCode().equals(that.getShareCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockExchangeName(), getTradingDayFormated(), getShareCode());
    }

    public Boolean isAvaliableInCashMarket() {
        String type = this.getShareCode().getType();
        return CODES_ALLOWED_CASH_MARKET.contains(type);
    }
}
