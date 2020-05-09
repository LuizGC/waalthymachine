package com.wealthy.machine.core.util;

import com.wealthy.machine.core.Config;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class DataFileGetter {

	private final File folder;
	private final String filename;
	private final Logger logger;

	public DataFileGetter(File folder, Config config) {
		this.folder = folder;
		this.logger = config.getLogger(this.getClass());
		this.filename = config.getDefaultFilename();
	}

	public File getFile(String key) {
		try {
			var keyFolder = new File(this.folder, key);
			keyFolder.mkdirs();
			var file = new File(keyFolder, this.filename);
			file.createNewFile();
			return file;
		} catch (IOException e) {
			this.logger.error("Error during accessing the data file for key: {}", key);
			throw new RuntimeException(e);
		}

	}


}
