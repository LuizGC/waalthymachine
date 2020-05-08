package com.wealthy.machine.core.seeker;

import com.wealthy.machine.core.quote.DailyQuote;

import java.net.URL;
import java.util.Set;

public interface DataSeeker {
    Set<DailyQuote> read(URL zipFileUrl);
}
