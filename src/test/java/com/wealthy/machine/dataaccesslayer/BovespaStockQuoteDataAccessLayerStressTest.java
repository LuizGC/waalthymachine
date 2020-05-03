package com.wealthy.machine.dataaccesslayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.ShareCode;
import com.wealthy.machine.util.BovespaDailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.wealthy.machine.StockExchange.BOVESPA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BovespaStockQuoteDataAccessLayerStressTest {

	private final String defaultFilename;

	public BovespaStockQuoteDataAccessLayerStressTest() {
		var config = new Config();
		this.defaultFilename = config.getDefaultFilename();
	}

	@Test
	public void testParallelismProcessingSavingQuotesInCorrectOrder() throws InterruptedException, IOException {
		for (var i = 0; i < 100; i++) {
			var whereToSave = Files.createTempDirectory("testShareCodeFileIsWorking_" + i).toFile();
			Executors
					.newCachedThreadPool()
					.invokeAll(generateCallableList(i, whereToSave))
					.stream()
					.map(this::processeFutureSet)
					.flatMap(Collection::stream)
					.collect(Collectors.groupingBy(DailyQuote::getShareCode))
					.forEach(testMemoryFileJsonAreEquals(whereToSave));
		}
	}

	private BiConsumer<ShareCode, List<DailyQuote>> testMemoryFileJsonAreEquals(File whereToSave) {
		return (shareCode, dailyQuotes) -> {
			try {
				var mapper = new ObjectMapper();
				var folderShareCodes = new File(BOVESPA.getFolder(whereToSave), shareCode.getCode());
				var fileShareCodes = new File(folderShareCodes, this.defaultFilename);
				var memoryJson = mapper.writeValueAsString(new TreeSet<>(dailyQuotes));
				var fileJson = Files.readString(fileShareCodes.toPath());
				assertEquals(memoryJson, fileJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	private Set<DailyQuote> processeFutureSet(Future<Set<DailyQuote>> future) {
		var set = new HashSet<DailyQuote>();
		try {
			set.addAll(future.get()) ;
		} catch (Exception e) {
			set.addAll(Collections.emptySet());
		}
		return set;
	}

	private ArrayList<Callable<Set<DailyQuote>>> generateCallableList(final int i, final File whereToSave) {
		var bovespaShareDataAccessLayer = new BovespaStockQuoteDataAccessLayer(whereToSave);
		var callableList = new ArrayList<Callable<Set<DailyQuote>>>();
		for (var j = 0; j < 5; j++) {
			var calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, j);
			calendar.add(Calendar.DAY_OF_MONTH, i);
			callableList.add(() -> {
				var tradingDay = calendar.getTime();
				var quotes = Set.of(
						createBovespaDailyQuote(tradingDay, "AAAA3"),
						createBovespaDailyQuote(tradingDay, "BBBB3"),
						createBovespaDailyQuote(tradingDay, "CCCC3"),
						createBovespaDailyQuote(tradingDay, "DDDD3")
				);
				bovespaShareDataAccessLayer.save(quotes);
				return Collections.unmodifiableSet(quotes);
			});
		}
		return callableList;
	}

	private DailyQuote createBovespaDailyQuote(Date tradingDay, String aaaa3) {
		return new BovespaDailyQuoteBuilder().shareCode(aaaa3).tradingDay(tradingDay).build();
	}
}

