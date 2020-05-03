package com.wealthy.machine.dataaccesslayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;
import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.util.BovespaDailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
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
	public void testParallelismProcessingSavingQuotesInCorrectOrder() throws IOException, InterruptedException {
		for (var i = 0; i < 50; i++) {
			var whereToSave = Files.createTempDirectory("testShareCodeFileIsWorking").toFile();
			var bovespaFolder = BOVESPA.getFolder(whereToSave);
			var bovespaShareDataAccessLayer = new BovespaStockQuoteDataAccessLayer(whereToSave);
			var callableList = new ArrayList<Callable<Set<DailyQuote>>>();

			for (var j = 0; j < 5; j++) {
				var calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR, j);
				calendar.add(Calendar.DAY_OF_MONTH, i);
				callableList.add(() -> {
					var tradingDay = calendar.getTime();
					var quotes = Set.of(
							new BovespaDailyQuoteBuilder().shareCode("AAAA3").tradingDay(tradingDay).build(),
							new BovespaDailyQuoteBuilder().shareCode("BBBB3").tradingDay(tradingDay).build(),
							new BovespaDailyQuoteBuilder().shareCode("CCCC3").tradingDay(tradingDay).build(),
							new BovespaDailyQuoteBuilder().shareCode("DDDD3").tradingDay(tradingDay).build()
					);
					bovespaShareDataAccessLayer.save(quotes);
					return Collections.unmodifiableSet(quotes);
				});
			}

			Executors
					.newCachedThreadPool()
					.invokeAll(callableList)
					.stream()
					.map(a -> {
						var set = new HashSet<DailyQuote>();
						try {
							set.addAll(a.get()) ;
						} catch (Exception e) {
							set.addAll(Collections.emptySet());
						}
						return set;
					})
					.flatMap(Collection::stream)
					.collect(Collectors.groupingBy(DailyQuote::getShareCode))
					.forEach(((shareCode, dailyQuotes) -> {
						try {
							var mapper = new ObjectMapper();
							var folderShareCodes = new File(bovespaFolder, shareCode.getCode());
							var fileShareCodes = new File(folderShareCodes, this.defaultFilename);
							var memoryJson = mapper.writeValueAsString(new TreeSet<>(dailyQuotes));
							var fileJson = Files.readString(fileShareCodes.toPath());
							assertEquals(memoryJson, fileJson);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}));
		}
	}
}

