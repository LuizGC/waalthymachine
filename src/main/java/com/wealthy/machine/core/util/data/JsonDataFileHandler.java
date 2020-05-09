package com.wealthy.machine.core.util.data;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.util.DataFileGetter;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class JsonDataFileHandler {

	private final DataFileGetter dataFileGetter;
	private final Logger logger;

	public JsonDataFileHandler(DataFileGetter dataFileGetter, Config config) {
		this.logger = config.getLogger(this.getClass());
		this.dataFileGetter = dataFileGetter;
	}

	public <T> void save(String key, Collection<T> items, Class<T> clazz) {
		save(key, items, clazz, null);
	}

	public <T> void save(String key, Collection<T> items, Class<T> clazz, Module module) {
		try {
			ObjectMapper mapper = getObjectMapper(module);
			var setToSave = new TreeSet<>(items);
			setToSave.addAll(list(key, clazz, module));
			mapper.writeValue(dataFileGetter.getFile(key), setToSave);
		} catch (IOException e) {
			this.logger.error("Error during saving {} key", key);
		}
	}

	public  <T> Set<T> list(String key, Class<T> clazz) {
		return list(key, clazz, null);
	}

	public  <T> Set<T> list(String key, Class<T> clazz, Module module) {
		try {
			ObjectMapper mapper = getObjectMapper(module);
			JavaType type = mapper.getTypeFactory().constructCollectionType(TreeSet.class, clazz);
			var quotesSet = mapper.<Set<T>>readValue(dataFileGetter.getFile(key), type);
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			this.logger.error("Error during listing {} key", key);
			throw new RuntimeException(e);
		}
	}

	private ObjectMapper getObjectMapper(Module module) {
		var mapper = new ObjectMapper();
		if (module != null) {
			mapper.registerModule(module);
		}
		return mapper;
	}

}
