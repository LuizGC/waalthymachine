package com.wealthy.machine.core.dataaccess;

import com.wealthy.machine.core.quote.DailyQuote;

import java.net.URL;
import java.util.Set;

public interface StockQuoteDataAccessLayer {

	void save(Set<DailyQuote> dailyQuoteSet);

	Set<URL> listUnsavedPaths();
}
