package com.wealthy.machine.dataaccess;

import com.wealthy.machine.quote.DailyShare;
import com.wealthy.machine.sharecode.ShareCode;

import java.util.Set;

public interface StockShareDataAccess {

	void save(Set<DailyShare> dailyShareSet);

	Set<DailyShare> list(ShareCode shareCode);

}
