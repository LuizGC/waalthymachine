package com.wealthy.machine.reader;

import com.wealthy.machine.quote.StockDailyQuote;

import java.util.Set;

public interface DataReader {
    Set<? extends StockDailyQuote> read(String zipFileUrl);
}
