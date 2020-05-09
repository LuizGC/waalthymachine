package com.wealthy.machine.core.quote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wealthy.machine.core.sharecode.ShareCode;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Date;

public interface DailyQuote extends Comparable<DailyQuote> {

    Date getTradingDay();

    @JsonIgnore
    ShareCode getShareCode();

    WealthNumber getOpenPrice();

    WealthNumber getClosePrice();

    WealthNumber getMinPrice();

    WealthNumber getMaxPrice();

    WealthNumber getAvgPrice();

    WealthNumber getVolume();

}
