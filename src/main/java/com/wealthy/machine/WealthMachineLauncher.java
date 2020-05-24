package com.wealthy.machine;

import com.wealthy.machine.bovespa.BovespaDataUpdaterUpdaterFacade;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.util.data.GitVersionControl;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class WealthMachineLauncher {

	public static void main(String... args) throws IOException, GitAPIException {
		var path = Files.createTempDirectory("storage_folder");
		var storageFolder = path.toFile();
		var config = new Config();
		var logger = config.getLogger(WealthMachineLauncher.class);
		logger.info("Data updating has started!");
		var git = new GitVersionControl(storageFolder, "bovespa", config);
		var dataUpdater = new BovespaDataUpdaterUpdaterFacade(storageFolder, config);
		var missingUrls = dataUpdater.listMissingUrl();
		var urlsDownloaded = missingUrls
				.stream()
				.map(url -> {
					dataUpdater.getMissingData(url);
					return url;
				})
				.collect(Collectors.toUnmodifiableSet());
		dataUpdater.updateDownloadedUrl(urlsDownloaded);
		dataUpdater.updateDownloadedShareCodes();
		logger.info("Data updating has finished!");
		git.push();
	}

}