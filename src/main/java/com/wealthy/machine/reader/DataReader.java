package com.wealthy.machine.reader;

import com.wealthy.machine.share.DailyShare;

import java.net.URL;
import java.util.Set;

public interface DataReader {
    Set<DailyShare> read(URL zipFileUrl);
}
