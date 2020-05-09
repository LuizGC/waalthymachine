package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuoteDeserializer;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.dataaccess.DataAccess;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.sharecode.ShareCode;
import com.wealthy.machine.core.util.DataFileGetter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BovespaDailyQuoteDataAccess implements DataAccess<BovespaDailyQuote> {

	private final Logger logger;
	private final DataFileGetter dataFileGetter;

	private final HashSet<BovespaShareCode> shareCodeDownloaded;

	public BovespaDailyQuoteDataAccess(DataFileGetter dataFileGetter, Config config) {
		this.logger = config.getLogger(this.getClass());
		this.dataFileGetter = dataFileGetter;
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
		try {
			var setToSave = new TreeSet<>(list(shareCode));
			setToSave.addAll(dailyQuotes);
			var mapper = new ObjectMapper();
			mapper.writeValue(getFile(shareCode), setToSave);
			this.shareCodeDownloaded.add(shareCode);
		} catch (IOException e) {
			this.logger.error("Error during saving: {} code", shareCode.getCode());
		}
	}

	private Set<DailyQuote> list(ShareCode shareCode) {
		try {
			var typeReference = new TypeReference<LinkedHashSet<BovespaDailyQuote>>() {};
			var mapper = new ObjectMapper();
			var module = new SimpleModule();
			module.addDeserializer(BovespaDailyQuote.class, new BovespaDailyQuoteDeserializer((BovespaShareCode) shareCode));
			mapper.registerModule(module);
			var quotesSet = mapper.readValue(getFile(shareCode), typeReference);
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

	private File getFile(ShareCode shareCode) {
		return dataFileGetter.getFile(shareCode.getCode());
	}

	public Set<BovespaShareCode> getShareCodeDownloaded() {
		return Collections.unmodifiableSet(shareCodeDownloaded);
	}

}