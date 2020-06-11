package com.wealthy.machine.core.util.technicalanlysis.type;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public enum SupportType {

	TIME((cleanMap) -> getFormattedString(cleanMap.get("tradingDay"))), VOLUME((cleanMap) -> cleanMap.get("volume"));

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private final Function<Map<String, String>, String> accessKey;

	SupportType(Function<Map<String, String>, String> accessKey) {
		this.accessKey = accessKey;
	}

	private static String getFormattedString(String date) {
		var tradingDay = new Date(Long.parseLong(date));
		return simpleDateFormat.format(tradingDay);
	}

	public String getValue(Map<String, String> cleanMap) {
		return this.accessKey.apply(cleanMap);
	}

	public String toString() {
		return this.name().toLowerCase();
	}

}
