package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.math.number.WealthNumber;
import com.wealthy.machine.sharecode.ShareCode;

import java.util.Date;

public interface DailyQuote extends Comparable {

    Date getTradingDay();

    ShareCode getShareCode();

    String getCompany();

    WealthNumber getOpenPrice();

    WealthNumber getClosePrice();

    WealthNumber getMinPrice();

    WealthNumber getMaxPrice();

    WealthNumber getAvgPrice();

    WealthNumber getVolume();

    StockExchange getStockExchangeName();

}
