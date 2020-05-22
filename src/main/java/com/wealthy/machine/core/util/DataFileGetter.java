package com.wealthy.machine.core.util;

import java.io.File;
import java.io.IOException;

public class DataFileGetter {

	private final File folder;

	public DataFileGetter(File folder) {
		this.folder = folder;
	}

	public File getFile(String key) throws IOException {
		var keyFolder = new File(this.folder, key);
		keyFolder.mkdirs();
		var file = new File(keyFolder, "datafile");
		file.createNewFile();
		return file;
	}
}