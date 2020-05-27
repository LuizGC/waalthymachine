package com.wealthy.machine.core;

import com.wealthy.machine.core.util.data.JsonDataFileHandler;

public interface DataUpdater {

	void execute(JsonDataFileHandler jsonDataFile, Config config);

	String getStockExchangeName();
}
