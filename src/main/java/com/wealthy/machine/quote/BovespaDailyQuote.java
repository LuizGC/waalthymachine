package com.wealthy.machine.quote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public final class BovespaDailyQuote implements DailyQuote {

    private final Date tradingDay;
    private final BovespaShareCode bovespaShareCode;
    private final String company;
    private final BigDecimal openPrice;
    private final BigDecimal closePrice;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final BigDecimal avgPrice;
    private final BigDecimal volume;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BovespaDailyQuote(
            @JsonProperty("tradingDay") Date tradingDay,
            @JsonProperty("shareCode") BovespaShareCode bovespaShareCode,
            @JsonProperty("company") String company,
            @JsonProperty("openPrice") BigDecimal openPrice,
            @JsonProperty("closePrice") BigDecimal closePrice,
            @JsonProperty("minPrice") BigDecimal minPrice,
            @JsonProperty("maxPrice") BigDecimal maxPrice,
            @JsonProperty("avgPrice") BigDecimal avgPrice,
            @JsonProperty("volume") BigDecimal volume
    ) {
        this.tradingDay = tradingDay;
        this.bovespaShareCode = bovespaShareCode;
        this.company = company;
        this.openPrice = openPrice.setScale(2);
        this.closePrice = closePrice.setScale(2);
        this.minPrice = minPrice.setScale(2);
        this.maxPrice = maxPrice.setScale(2);
        this.avgPrice = avgPrice.setScale(2);
        this.volume = volume.setScale(2);
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
    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    @Override
    public BigDecimal getClosePrice() {
        return closePrice;
    }

    @Override
    public BigDecimal getMinPrice() {
        return minPrice;
    }

    @Override
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    @Override
    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    @Override
    public BigDecimal getVolume() {
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
