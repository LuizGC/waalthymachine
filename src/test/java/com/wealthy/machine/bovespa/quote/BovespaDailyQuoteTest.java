package com.wealthy.machine.bovespa.quote;

import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static java.util.Calendar.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BovespaDailyQuoteTest {

	@Test
	public void equals_WhenYearAndShareCodeAreSame_ShouldBeEquals() {
		var firstQuote = new BovespaDailyQuoteBuilder()
				.avgPrice("10.32")
				.minPrice("50.30")
				.openPrice("28.35")
				.build();
		var secondQuote = new BovespaDailyQuoteBuilder()
				.avgPrice("30.78")
				.build();
		assertEquals(firstQuote, secondQuote);
		assertEquals(firstQuote.hashCode(), secondQuote.hashCode());
	}

	@Test
	public void equals_WhenDifferentTradingDate_ShouldNotBeEquals() {
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1989);
		var firstQuote = new BovespaDailyQuoteBuilder()
				.tradingDay(calendar.getTime())
				.build();
		calendar.set(Calendar.YEAR, 2010);
		var secondQuote = new BovespaDailyQuoteBuilder()
				.tradingDay(calendar.getTime())
				.build();
		assertNotEquals(firstQuote, secondQuote);
		assertNotEquals(firstQuote.hashCode(), secondQuote.hashCode());
	}

	@Test
	public void equals_WhenDifferentShareCode_ShouldNotBeEquals() {
		var firstQuote = new BovespaDailyQuoteBuilder()
				.shareCode("ABCD1")
				.build();
		var secondQuote = new BovespaDailyQuoteBuilder()
				.shareCode("ZXCV1")
				.build();
		secondQuote = new BovespaDailyQuoteBuilder().build();
		assertNotEquals(firstQuote, secondQuote);
		assertNotEquals(firstQuote.hashCode(), secondQuote.hashCode());
	}

	@Test
	public void compareTo_WhenShareCodeAreDifferent_ShouldReturnOne() {
		var firstShareCode = "ABCD1";
		var firstQuote = new BovespaDailyQuoteBuilder()
				.shareCode(firstShareCode)
				.build();
		var secondShareCode = "ZXCV1";
		var secondQuote = new BovespaDailyQuoteBuilder()
				.shareCode(secondShareCode)
				.build();
		assertEquals(firstShareCode.compareTo(secondShareCode), firstQuote.compareTo(secondQuote));
	}

	@Test
	public void compareTo_WhenHoursAreDifferentButDateAreEquals_ShouldReturnZero() {
		var calendar = Calendar.getInstance();
		calendar.set(1989, JUNE, 26, 20, 0);
		var firstQuote = new BovespaDailyQuoteBuilder()
				.tradingDay(calendar.getTime())
				.build();
		calendar.set(1989, JUNE, 26, 21, 0);
		var secondQuote = new BovespaDailyQuoteBuilder()
				.tradingDay(calendar.getTime())
				.build();
		assertEquals(0, firstQuote.compareTo(secondQuote));
	}

	@Test
	public void compareTo_WhenDateIsBiggestDate_ShouldReturnOne() {
		var firstQuote = new BovespaDailyQuoteBuilder()
				.build();
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1989);
		var secondQuote = new BovespaDailyQuoteBuilder()
				.tradingDay(calendar.getTime())
				.build();
		assertEquals(1, firstQuote.compareTo(secondQuote));
	}

}
