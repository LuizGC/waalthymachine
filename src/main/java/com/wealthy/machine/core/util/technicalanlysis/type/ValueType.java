package com.wealthy.machine.core.util.technicalanlysis.type;

import java.util.Map;

public enum ValueType {

	OPEN("openPrice"), CLOSE("closePrice"), LOW("lowPrice"), HIGH("highPrice");

	private final String accessKey;

	ValueType(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getValue(Map<String, String> cleanMap) {
		return cleanMap.get(accessKey);
	}

	public String toString() {
		return this.name().toLowerCase();
	}

	public String getJsonFileAccessKey() {
		return accessKey;
	}
}
