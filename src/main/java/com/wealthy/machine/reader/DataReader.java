package com.wealthy.machine.reader;

import com.wealthy.machine.quote.StockDailyShare;

import java.net.URL;
import java.util.Set;

public interface DataReader {
    Set<? extends StockDailyShare> read(URL zipFileUrl);
}
