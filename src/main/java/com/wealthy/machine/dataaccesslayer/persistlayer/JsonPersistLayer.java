package com.wealthy.machine.dataaccesslayer.persistlayer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;

import java.io.File;
import java.io.IOException;

public class JsonPersistLayer<T> implements PersistLayer{

	private final File folder;
	private final ObjectMapper mapper;
	private final String filename;
	private final TypeReference<T> type;

	public JsonPersistLayer(File folder, TypeReference<T> typeReference) {
		var config = new Config();
		var logger = config.getLogger(this.getClass());
		if (!folder.isDirectory()) {
			logger.error("The folder to persist the data must be a folder.");
			throw new RuntimeException();
		}
		this.folder = folder;
		this.mapper = new ObjectMapper();

		this.filename = config.getDefaultFilename();
		this.type = typeReference;
	}

	@Override
	public T readFileContent(String key) throws IOException {
		return mapper.readValue(getFile(key), type);
	}

	@Override
	public void saveData(String key, Object data) throws IOException {
		mapper.writeValue(getFile(key), data);
	}

	private File getFile(Object key) throws IOException {
		var keyFolder = new File(this.folder, key.toString());
		keyFolder.mkdirs();
		var file = new File(keyFolder, this.filename);
		file.createNewFile();
		return file;
	}
}
