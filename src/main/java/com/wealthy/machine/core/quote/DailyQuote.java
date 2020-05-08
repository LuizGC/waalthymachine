package com.wealthy.machine.core.quote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wealthy.machine.core.StockExchange;
import com.wealthy.machine.core.math.number.WealthNumber;
import com.wealthy.machine.core.sharecode.ShareCode;

import java.util.Date;

public interface DailyQuote extends Comparable {

    Date getTradingDay();

    @JsonIgnore
    ShareCode getShareCode();

    WealthNumber getOpenPrice();

    WealthNumber getClosePrice();

    WealthNumber getMinPrice();

    WealthNumber getMaxPrice();

    WealthNumber getAvgPrice();

    WealthNumber getVolume();

    StockExchange getStockExchangeName();

}
