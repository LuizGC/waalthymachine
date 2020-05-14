package com.wealthy.machine;

import com.wealthy.machine.bovespa.BovespaDataUpdaterUpdaterFacade;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.DataUpdaterFacade;
import com.wealthy.machine.core.util.data.GitVersionControl;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class WealthMachineLauncher {

	private static class ParallelDownloader implements Callable<URL> {

		private final DataUpdaterFacade dataUpdater;
		private final URL url;
		private final Logger logger;

		private ParallelDownloader(DataUpdaterFacade dataUpdater, URL url, Logger logger) {
			this.dataUpdater = dataUpdater;
			this.url = url;
			this.logger = logger;
		}

		@Override
		public URL call() {
			logger.info("Starting process the {}", url);
			var missingData = dataUpdater.getMissingData(url);
			dataUpdater.save(missingData);
			logger.info("Finishing process the {}", url);
			return url;
		}
	}

	private static Set<URL> execute(Set<ParallelDownloader> tasks) throws InterruptedException {
		var totalTasks = tasks.size();
		var totalProcessors = Runtime.getRuntime().availableProcessors();
		var executor = Executors.newFixedThreadPool(Math.min(totalTasks, totalProcessors*6));
		var futures = executor.invokeAll(tasks);
		executor.shutdown();
		return futures.stream()
				.map(WealthMachineLauncher::getFuture)
				.collect(Collectors.toUnmodifiableSet());
	}

	private static URL getFuture(Future<URL> urlFuture) {
		try {
			return urlFuture.get();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String... args) throws IOException, InterruptedException, GitAPIException {
		var path = Files.createTempDirectory("storage_folder");
		var storageFolder = path.toFile();
		var config = new Config();
		var git = new GitVersionControl(storageFolder, "bovespa", config);
		var logger = config.getLogger(WealthMachineLauncher.class);
		logger.info("Data updating has started!");
		var dataUpdater = new BovespaDataUpdaterUpdaterFacade(storageFolder, config);
		var missingUrls = dataUpdater.listMissingUrl();
		var tasks = missingUrls
				.stream()
				.map(url -> new ParallelDownloader(dataUpdater, url, logger))
				.collect(Collectors.toUnmodifiableSet());
		var urlsDownloaded = execute(tasks);
		dataUpdater.updateDownloadedUrl(urlsDownloaded);
		dataUpdater.updateDownloadedShareCodes();
		logger.info("Data updating has finished!");
		logger.info("All data were on {} folder", storageFolder);
		git.push();
	}

}
