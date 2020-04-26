package com.wealthy.machine.quote;

import com.wealthy.machine.StockExchange;
import com.wealthy.machine.sharecode.ShareCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public interface DailyQuote extends Serializable, Comparable {

    Date getTradingDay();

    ShareCode getShareCode();

    String getCompany();

    BigDecimal getOpenPrice();

    BigDecimal getClosePrice();

    BigDecimal getMinPrice();

    BigDecimal getMaxPrice();

    BigDecimal getAvgPrice();

    BigInteger getVolume();

    StockExchange getStockExchangeName();

}
