package com.wealthy.machine.reader;

import com.wealthy.machine.quote.DailyQuote;

import java.net.URL;
import java.util.Set;

public interface DataReader {
    Set<DailyQuote> read(URL zipFileUrl);
}
