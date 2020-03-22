package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Data
public class BovespaStockDailyQuote implements StockDailyQuote {

    private final Date tradingDay;
    private final String stockCode;
    private final String company;
    private final Double openPrice;
    private final Double closePrice;
    private final Double minPrice;
    private final Double maxPrice;
    private final Double avgPrice;
    private final Double volume;

    public StockExchange getStockExchangeName() {
        return StockExchange.BOVESPA;
    }

    public BovespaStockDailyQuote(String line) {
        line = line.trim();
        if(line.length() != 245){
            throw new RuntimeException("Line size must be 245!");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            this.tradingDay = dateFormat.parse(line.substring(2, 10).trim());
        } catch (ParseException e) {
            throw new RuntimeException("Date is invalid!", e);
        }
        this.stockCode = readString(line, 12, 24);
        this.company = readString(line, 27, 39);
        this.openPrice = readDouble(line, 56, 69);
        this.closePrice = readDouble(line, 108, 121);
        this.minPrice = readDouble(line, 82, 95);
        this.maxPrice = readDouble(line, 69, 82);
        this.avgPrice = readDouble(line, 97, 108);
        this.volume = readDouble(line, 170, 188);
    }

    private String readString(String line, Integer begin, Integer end) {
        return line.substring(begin, end).trim();
    }

    private Double readDouble(String line, Integer begin, Integer end) {
        return  Double.parseDouble(readString(line, begin, end))/100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BovespaStockDailyQuote that = (BovespaStockDailyQuote) o;
        return getStockExchangeName().equals(that.getStockExchangeName()) &&
                getTradingDay().equals(that.getTradingDay()) &&
                getStockCode().equals(that.getStockCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockExchangeName(), getTradingDay(), getStockCode());
    }
}
