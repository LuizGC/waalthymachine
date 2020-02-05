package com.wealthy.machine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockDailyQuote {

    private final Date tradingDay;
    private final String stockCode;
    private final String company;
    private final Double openPrice;
    private final Double closePrice;
    private final Double minPrice;
    private final Double maxPrice;
    private final Double avgPrice;

    public StockDailyQuote(String line) {
        line = line.trim();
        if(line.length() != 245){
            throw new RuntimeException("Line size must be 245!");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            this.tradingDay = dateFormat.parse(line.substring(2, 10).trim());
        } catch (ParseException e) {
            throw new RuntimeException("Date is invalid!");
        }
        this.stockCode = readString(line, 12, 24);
        this.company = readString(line, 27, 39);
        this.openPrice = readDouble(line, 56, 69);
        this.closePrice = readDouble(line, 108, 121);
        this.minPrice = readDouble(line, 82, 95);
        this.maxPrice = readDouble(line, 69, 82);
        this.avgPrice = readDouble(line, 97, 108);
    }

    private String readString(String line, Integer begin, Integer end) {
        return line.substring(begin, end).trim();
    }

    private Double readDouble(String line, Integer begin, Integer end) {
        return  Double.parseDouble(line.substring(begin, end).trim())/100;
    }

    @Override
    public String toString() {
        return "StockDailyQuote{" +
                "tradingDay=" + tradingDay +
                ", stockCode='" + stockCode + '\'' +
                ", company='" + company + '\'' +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", avgPrice=" + avgPrice +
                '}';
    }
}
