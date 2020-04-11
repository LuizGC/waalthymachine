package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public final class BovespaStockDailyShare implements StockDailyShare {

    //List with codes of possible cash market stocks: 3 is ON, 4 is PN or 11 UNIT
    //It must works for fractional share market: 3F, 4F, 11F
    private final static Set<String> CODES_ALLOWED_CASH_MARKET = Set.of("3", "3F", "4", "4F", "11", "11F");

    private final Date tradingDay;
    private final String stockCode;
    private final String company;
    private final Double openPrice;
    private final Double closePrice;
    private final Double minPrice;
    private final Double maxPrice;
    private final Double avgPrice;
    private final Double volume;

    public BovespaStockDailyShare(Date tradingDay, String stockCode, String company, Double openPrice, Double closePrice, Double minPrice, Double maxPrice, Double avgPrice, Double volume) {
        this.tradingDay = tradingDay;
        this.stockCode = stockCode;
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
    public String getStockCode() {
        return stockCode;
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
        BovespaStockDailyShare that = (BovespaStockDailyShare) o;
        return getStockExchangeName().equals(that.getStockExchangeName()) &&
                getTradingDay().equals(that.getTradingDay()) &&
                getStockCode().equals(that.getStockCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockExchangeName(), getTradingDay(), getStockCode());
    }

    public Boolean isAvaliableInCashMarket() {
        String type = this.getStockCode().substring(4);
        return CODES_ALLOWED_CASH_MARKET.contains(type);
    }
}
