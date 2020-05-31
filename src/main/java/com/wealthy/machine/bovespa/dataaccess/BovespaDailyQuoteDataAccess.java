package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuoteDeserializer;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuoteSerializer;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BovespaDailyQuoteDataAccess {

	private final JsonDataFileHandler jsonDataFileHandler;

	private final Set<BovespaShareCode> shareCodeDownloaded;

	public BovespaDailyQuoteDataAccess(JsonDataFileHandler jsonDataFileHandler) {
		this.jsonDataFileHandler = jsonDataFileHandler;
		this.shareCodeDownloaded = new TreeSet<>();
	}

	public synchronized void save(Set<BovespaDailyQuote> dailyQuoteSet) {
			dailyQuoteSet
					.stream()
					.collect(Collectors.groupingBy(BovespaDailyQuote::getShareCode))
					.forEach(this::saveBovespaDailyQuote);
	}

	private void saveBovespaDailyQuote(BovespaShareCode shareCode, List<BovespaDailyQuote> dailyQuotes) {
		var module = new SimpleModule();
		module.addDeserializer(BovespaDailyQuote.class, new BovespaDailyQuoteDeserializer(shareCode));
		module.addSerializer(BovespaDailyQuote.class, new BovespaDailyQuoteSerializer());
		this.jsonDataFileHandler.append(shareCode.getCode(), dailyQuotes, BovespaDailyQuote.class, module);
		this.shareCodeDownloaded.add(shareCode);
	}

	public Set<BovespaShareCode> listDownloadedShareCode() {
		return Collections.unmodifiableSet(shareCodeDownloaded);
	}

}