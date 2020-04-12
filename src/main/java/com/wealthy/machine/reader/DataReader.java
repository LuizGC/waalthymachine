package com.wealthy.machine.reader;

import com.wealthy.machine.quote.DailyShare;

import java.net.URL;
import java.util.Set;

public interface DataReader {
    Set<DailyShare> read(URL zipFileUrl);
}
