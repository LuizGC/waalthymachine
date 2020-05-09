package com.wealthy.machine;

import com.wealthy.machine.bovespa.BovespaDataUpdaterUpdaterFacade;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.DataUpdaterFacade;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class WealthMachineLauncher {

	private static class ParallelDownloader implements Callable<URL> {

		private final DataUpdaterFacade dataUpdater;
		private final URL url;

		private ParallelDownloader(DataUpdaterFacade dataUpdater, URL url) {
			this.dataUpdater = dataUpdater;
			this.url = url;
		}

		@Override
		public URL call() {
			var missingData = dataUpdater.getMissingData(url);
			dataUpdater.save(missingData);
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

	public static void main(String... args) throws IOException, InterruptedException {
		var path = Files.createTempDirectory("storage_folder");
		var storageFolder = path.toFile();
		System.setProperty("LOG_FOLDER", storageFolder.toString());
		var config = new Config();
		var logger = config.getLogger(WealthMachineLauncher.class);
		logger.info("Data updating has started!");
		var dataUpdater = new BovespaDataUpdaterUpdaterFacade(storageFolder, config);
		var missingUrls = dataUpdater.listMissingUrl();
		var tasks = missingUrls
				.stream()
				.map(url -> new ParallelDownloader(dataUpdater, url))
				.collect(Collectors.toUnmodifiableSet());
		var urlsDownloaded = execute(tasks);
		dataUpdater.updateDownloadedUrl(urlsDownloaded);
		dataUpdater.updateDownloadedShareCodes();
		logger.info("Data updating has finished!");
		logger.info("All data were on {} folder", storageFolder);
	}

}
