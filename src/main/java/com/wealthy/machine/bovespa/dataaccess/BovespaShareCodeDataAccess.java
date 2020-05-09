package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCodeDeserializer;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCodeSerializer;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.dataaccess.DataAccess;
import com.wealthy.machine.core.util.DataFileGetter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BovespaShareCodeDataAccess implements DataAccess<BovespaShareCode> {
	private final File fileShareCodes;
	private final Logger logger;

	public BovespaShareCodeDataAccess(DataFileGetter dataFileGetter, Config config) {
		this.logger = config.getLogger(config.getClass());
		this.fileShareCodes = dataFileGetter.getFile(config.getShareCodeKey());
	}

	@Override
	public synchronized void save(Set<BovespaShareCode> shareCodes) {
		try {
			var setSave = new HashSet<>(shareCodes);
			setSave.addAll(listShareCodesSaved());
			var mapper = new ObjectMapper();
			mapper.writeValue(this.fileShareCodes, setSave);
		} catch (IOException e) {
			this.logger.error("Error during saving share codes downloaded.");
			throw new RuntimeException(e);
		}
	}

	private Set<BovespaShareCode> listShareCodesSaved() {
		try{
			var typeReference = new TypeReference<HashSet<BovespaShareCode>>() {};
			var mapper = new ObjectMapper();
			var module = new SimpleModule();
			module.addDeserializer(BovespaShareCode.class, new BovespaShareCodeDeserializer());
			module.addSerializer(BovespaShareCode.class, new BovespaShareCodeSerializer());
			mapper.registerModule(module);
			var shareCodes = mapper.readValue(this.fileShareCodes, typeReference);
			return Collections.unmodifiableSet(shareCodes);
		} catch (Exception e) {
			return Collections.emptySet();
		}
	}
}
