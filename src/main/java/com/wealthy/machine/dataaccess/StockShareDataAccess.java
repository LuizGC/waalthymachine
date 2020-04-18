package com.wealthy.machine.dataaccess;

import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;

import java.util.Set;

public interface StockShareDataAccess {

	void save(Set<DailyQuote> dailyQuoteSet);

	Set<DailyQuote> list(ShareCode shareCode);

}
