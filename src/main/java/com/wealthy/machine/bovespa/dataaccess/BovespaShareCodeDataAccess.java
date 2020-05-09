package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCodeDeserializer;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCodeSerializer;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.dataaccess.DataAccess;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;

import java.util.Set;

public class BovespaShareCodeDataAccess implements DataAccess<BovespaShareCode> {

	private final JsonDataFileHandler jsonDataFile;
	private final String shareCodeKey;

	public BovespaShareCodeDataAccess(JsonDataFileHandler jsonDataFile, Config config) {
		this.jsonDataFile = jsonDataFile;
		this.shareCodeKey = config.getShareCodeKey();
	}

	@Override
	public synchronized void save(Set<BovespaShareCode> shareCodes) {
		var module = new SimpleModule();
		module.addDeserializer(BovespaShareCode.class, new BovespaShareCodeDeserializer());
		module.addSerializer(BovespaShareCode.class, new BovespaShareCodeSerializer());
		jsonDataFile.save(this.shareCodeKey, shareCodes, BovespaShareCode.class, module);
	}

}
