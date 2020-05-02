package com.wealthy.machine;

import com.wealthy.machine.datamanager.BovespaDataManager;
import com.wealthy.machine.datamanager.DataManagerCommander;

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
			new BovespaDataManager(storageFolder)
		};
		for (var dataDownloader : dataDownloaderList) {
			dataDownloader.execute();
		}
		logger.info("storageFolder={}", storageFolder);
	}

}
