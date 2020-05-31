package com.wealthy.machine.core.quote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wealthy.machine.core.sharecode.ShareCode;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Date;

public interface DailyQuote {

    Date getTime();

    @JsonIgnore
    ShareCode getShareCode();

    WealthNumber getOpen();

    WealthNumber getClose();

    WealthNumber getLow();

    WealthNumber getHigh();

    WealthNumber getAvgPrice();

    WealthNumber getVolume();

}
