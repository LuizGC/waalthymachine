package com.wealthy.machine;

import com.wealthy.machine.bovespa.BovespaDataUpdater;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.DataUpdater;
import com.wealthy.machine.core.util.DataFileGetter;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;

import java.io.IOException;
import java.nio.file.Files;

public class WealthMachineLauncher {

	public static void main(String... args) throws IOException {
		var config = new Config();
		var logger = config.getLogger(WealthMachineLauncher.class);
		logger.info("Data updating has started!");
		var dataUpdaters = new DataUpdater[]{new BovespaDataUpdater()};
		for (DataUpdater dataUpdater : dataUpdaters) {
			logger.info("Starting={}", dataUpdater.getClass());
			var storageFolder = Files.createTempDirectory("storage_folder").toFile();
			var dataFileGetter = new DataFileGetter(storageFolder);
			var dataFileHandler = new JsonDataFileHandler(dataFileGetter);
			dataUpdater.execute(dataFileHandler, config);
			logger.info("Ended={}", dataUpdater.getClass());
		}
		logger.info("Data updating has finished!");
	}

}