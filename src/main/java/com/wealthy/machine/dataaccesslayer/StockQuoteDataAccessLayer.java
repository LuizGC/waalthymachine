package com.wealthy.machine.dataaccesslayer;

import com.wealthy.machine.quote.DailyQuote;

import java.net.URL;
import java.util.Set;

public interface StockQuoteDataAccessLayer {

	void save(Set<DailyQuote> dailyQuoteSet);

	Set<URL> listUnsavedPaths();
}
