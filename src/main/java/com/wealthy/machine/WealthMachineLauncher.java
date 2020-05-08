package com.wealthy.machine;

import com.wealthy.machine.bovespa.processor.BovespaDataProcessor;
import com.wealthy.machine.core.datamanager.DataManagerCommander;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WealthMachineLauncher {

	public static void main(String... args) throws IOException {
		var path = args.length == 0 ? Files.createTempDirectory("storage_folder") : Path.of(args[0]);
		var storageFolder = path.toFile();
		System.setProperty("LOG_FOLDER", storageFolder.toString());
		var config = new Config();
		var logger = config.getLogger(WealthMachineLauncher.class);
		var dataDownloaderList = new DataManagerCommander[]{
			new BovespaDataProcessor(storageFolder)
		};
		for (var dataDownloader : dataDownloaderList) {
			dataDownloader.getMissingData();
		}
		logger.info("storageFolder={}", storageFolder);
	}

}
