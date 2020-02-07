package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;

import java.util.Date;

public interface StockDailyQuote {

    Date getTradingDay();

    String getStockCode();

    String getCompany();

    Double getOpenPrice();

    Double getClosePrice();

    Double getMinPrice();

    Double getMaxPrice();

    Double getAvgPrice();

    Double getVolume();

    StockExchange getStockExchangeName();

}
