package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuoteDeserializer;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.dataaccess.DataAccess;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BovespaDailyQuoteDataAccess implements DataAccess<BovespaDailyQuote> {

	private final JsonDataFileHandler jsonDataFileHandler;

	private final HashSet<BovespaShareCode> shareCodeDownloaded;

	public BovespaDailyQuoteDataAccess(JsonDataFileHandler jsonDataFileHandler) {
		this.jsonDataFileHandler = jsonDataFileHandler;
		this.shareCodeDownloaded = new HashSet<>();
	}

	@Override
	public synchronized void save(Set<BovespaDailyQuote> dailyQuoteSet) {
			dailyQuoteSet
					.stream()
					.collect(Collectors.groupingBy(BovespaDailyQuote::getShareCode))
					.forEach(this::saveBovespaDailyQuote);
	}

	private void saveBovespaDailyQuote(BovespaShareCode shareCode, List<BovespaDailyQuote> dailyQuotes) {
		var module = new SimpleModule();
		module.addDeserializer(BovespaDailyQuote.class, new BovespaDailyQuoteDeserializer(shareCode));
		this.jsonDataFileHandler.save(shareCode.getCode(), dailyQuotes, BovespaDailyQuote.class, module);
		this.shareCodeDownloaded.add(shareCode);
	}

	public Set<BovespaShareCode> getShareCodeDownloaded() {
		return Collections.unmodifiableSet(shareCodeDownloaded);
	}

}