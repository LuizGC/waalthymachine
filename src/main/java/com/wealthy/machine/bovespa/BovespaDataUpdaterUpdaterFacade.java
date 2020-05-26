package com.wealthy.machine.bovespa;


import com.wealthy.machine.bovespa.dataaccess.BovespaDailyQuoteDataAccess;
import com.wealthy.machine.bovespa.dataaccess.BovespaShareCodeDataAccess;
import com.wealthy.machine.bovespa.dataaccess.BovespaUrlDataAccess;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.seeker.BovespaDataSeeker;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.DataUpdaterFacade;
import com.wealthy.machine.core.util.DataFileGetter;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;

import java.io.File;
import java.net.URL;
import java.util.Set;

public class BovespaDataUpdaterUpdaterFacade implements DataUpdaterFacade {

	private final BovespaUrlDataAccess urlDataAccess;
	private final BovespaDailyQuoteDataAccess dailyQuoteDataAccess;
	private final BovespaDataSeeker dataSeeker;
	private final BovespaShareCodeDataAccess shareCodeDataAccess;

	public BovespaDataUpdaterUpdaterFacade(File storageFolder, Config config) {
		var fileGetter = new DataFileGetter(storageFolder);
		var jsonDataFile = new JsonDataFileHandler(fileGetter, config);
		this.dataSeeker = new BovespaDataSeeker(config);
		this.dailyQuoteDataAccess = new BovespaDailyQuoteDataAccess(jsonDataFile);
		this.shareCodeDataAccess = new BovespaShareCodeDataAccess(jsonDataFile);
		this.urlDataAccess = new BovespaUrlDataAccess(jsonDataFile, config);
	}

	@Override
	public Set<URL> listMissingUrl() {
		return this.urlDataAccess.listMissingUrl();
	}

	@Override
	public Set<BovespaDailyQuote> getMissingData(URL zipFileUrl) {
		return this.dataSeeker.read(zipFileUrl);
	}

	@Override
	public void save(Set<BovespaDailyQuote> dailyQuoteSet) {
		this.dailyQuoteDataAccess.save(dailyQuoteSet);
	}

	@Override
	public void updateDownloadedUrl(Set<URL> urls) {
		this.urlDataAccess.save(urls);
	}

	@Override
	public void updateDownloadedShareCodes() {
		var shareCodes = this.dailyQuoteDataAccess.getShareCodeDownloaded();
		this.shareCodeDataAccess.save(shareCodes);
	}
}

