package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

class BovespaDailyQuoteDataAccessTest {

	@Test
	void save_WhenDataSetHasTwoDailyQuoteWithDifferentShareCode_ShouldCallSaveMethodTwice() {
		var jsonDataFileHandler = mock(JsonDataFileHandler.class);
		var dataAccess = new BovespaDailyQuoteDataAccess(jsonDataFileHandler);
		var dataSet = Set.of(
				new BovespaDailyQuoteBuilder().build(),
				new BovespaDailyQuoteBuilder()
						.shareCode("SANB11")
						.build()
		);
		dataAccess.save(dataSet);
		verify(jsonDataFileHandler, times(2))
				.save(anyString(), anyCollection(), eq(BovespaDailyQuote.class), any(SimpleModule.class));
	}

	@Test
	void listDownloadedShareCode_WhenAddTwoShareCodes_ShouldReturnTwoShareCodes() {
		var jsonDataFileHandler = mock(JsonDataFileHandler.class);
		var dataAccess = new BovespaDailyQuoteDataAccess(jsonDataFileHandler);
		var firstShareCode = new BovespaShareCode("SANB11");
		var secondShareCode = new BovespaShareCode("ITUB4");
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2005);
		var dataSet = Set.of(
				new BovespaDailyQuoteBuilder()
						.shareCode(firstShareCode.getCode())
						.build(),
				new BovespaDailyQuoteBuilder()
						.shareCode(secondShareCode.getCode())
						.build(),
				new BovespaDailyQuoteBuilder()
						.shareCode(secondShareCode.getCode())
						.tradingDay(calendar.getTime())
						.build()
		);
		dataAccess.save(dataSet);
		assertIterableEquals(
				new TreeSet<>(Set.of(
						firstShareCode,
						secondShareCode
				)),
				dataAccess.listDownloadedShareCode()
		);
	}
}