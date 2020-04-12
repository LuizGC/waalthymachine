package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.ShareCode;

import java.util.Date;

public interface StockDailyShare {

    Date getTradingDay();

    ShareCode getBovespaShareCode();

    String getCompany();

    Double getOpenPrice();

    Double getClosePrice();

    Double getMinPrice();

    Double getMaxPrice();

    Double getAvgPrice();

    Double getVolume();

    StockExchange getStockExchangeName();

}
