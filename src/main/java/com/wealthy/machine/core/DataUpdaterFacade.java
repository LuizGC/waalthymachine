package com.wealthy.machine.core;

import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;

import java.net.URL;
import java.util.Set;

public interface DataUpdaterFacade {

	Set<URL> listMissingUrl();

	Set<BovespaDailyQuote> getMissingData(URL zipFileUrl);

	void save(Set<BovespaDailyQuote> dailyQuoteSet);

	void updateDownloadedUrl(Set<URL> dailyQuoteSet);

	void updateDownloadedShareCodes();

}
