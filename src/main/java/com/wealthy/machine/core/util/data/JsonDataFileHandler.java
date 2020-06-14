package com.wealthy.machine.core.util.data;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.core.util.DataFileGetter;

import java.io.IOException;
import java.util.*;

public class JsonDataFileHandler {

	private final DataFileGetter dataFileGetter;

	public JsonDataFileHandler(DataFileGetter dataFileGetter) {
		this.dataFileGetter = dataFileGetter;
	}

	public <T extends Comparable<T>> void append(String key, Collection<T> items, Class<T> clazz) {
		append(key, items, clazz, null);
	}

	public <T extends Comparable<T>> void append(String key, Collection<T> items, Class<T> clazz, Module module) {
		var setToSave = new TreeSet<>(items);
		setToSave.addAll(list(key, clazz, module));
		override(key, setToSave, module);
	}

	public <T> void override(String key, Collection<T> items, Module module) {
		try {
			ObjectMapper mapper = getObjectMapper(module);
			var setToSave = new ArrayList<>(items);
			mapper.writeValue(dataFileGetter.getFile(key), setToSave);
		} catch (IOException e) {
			throw new RuntimeException("Error during saving " + key, e);
		}
	}

	public <T> void override(String key, Collection<T> items) {
		this.override(key, items, null);
	}

	public <T extends Comparable<T>> Set<T> list(String key, Class<T> clazz) {
		return list(key, clazz, null);
	}

	public <T extends Comparable<T>> Set<T> list(String key, Class<T> clazz, Module module) {
		try {
			ObjectMapper mapper = getObjectMapper(module);
			JavaType type = mapper.getTypeFactory().constructCollectionType(TreeSet.class, clazz);
			var file = dataFileGetter.getFile(key);
			var quotesSet = new TreeSet<T>();
			if (file.length() > 0) {
				quotesSet = mapper.readValue(file, type);
			}
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			throw new RuntimeException("Error during listing " + key, e);
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
