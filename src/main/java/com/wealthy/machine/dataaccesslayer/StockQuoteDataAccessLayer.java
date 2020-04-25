package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

public interface StockQuoteDataAccessLayer {

	void save(Set<DailyQuote> dailyQuoteSet);

	Set<DailyQuote> list(ShareCode shareCode);

	Collection<URL> listUnsavedPaths();
}
