package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.ShareCode;

import java.io.Serializable;
import java.util.Date;

public interface DailyQuote extends Serializable {

    Date getTradingDay();

    ShareCode getShareCode();

    String getCompany();

    Double getOpenPrice();

    Double getClosePrice();

    Double getMinPrice();

    Double getMaxPrice();

    Double getAvgPrice();

    Double getVolume();

    StockExchange getStockExchangeName();

}
