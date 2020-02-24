package com.wealthy.machine.reader;

import com.wealthy.machine.quote.StockDailyQuote;

import java.util.stream.Stream;

public interface DataReader {
    Stream<? extends StockDailyQuote> read();
}
