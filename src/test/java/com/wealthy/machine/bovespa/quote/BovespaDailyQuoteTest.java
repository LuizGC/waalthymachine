package com.wealthy.machine.bovespa.quote;

import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BovespaDailyQuoteTest {

	@Test
	public void testEqulas(){
		var random = new Random();
		var firstQuote = new BovespaDailyQuoteBuilder().build();
		var secondQuote = new BovespaDailyQuoteBuilder()
				.avgPrice(String.valueOf(random.nextDouble()))
				.build();
		assertEquals(firstQuote, secondQuote, "Those quotes must be equals!");
		secondQuote = new BovespaDailyQuoteBuilder().shareCode("ABCQ1").build();
		assertNotEquals(firstQuote, secondQuote, "Those quotes are different!");
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1989);
		secondQuote = new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime()).build();
		assertNotEquals(firstQuote, secondQuote, "Those quotes are different!");
	}

	@Test
	public void testHashCode(){
		var random = new Random();
		var firstQuote = new BovespaDailyQuoteBuilder().build();
		var secondQuote = new BovespaDailyQuoteBuilder()
				.avgPrice(String.valueOf(random.nextDouble()))
				.build();
		assertEquals(firstQuote.hashCode(), secondQuote.hashCode(), "Those quotes hash code must be equals!");
		secondQuote = new BovespaDailyQuoteBuilder().shareCode("ABCQ1").build();
		assertNotEquals(firstQuote.hashCode(), secondQuote.hashCode(), "Those quotes hash code are different!");
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1989);
		secondQuote = new BovespaDailyQuoteBuilder().tradingDay(calendar.getTime()).build();
		assertNotEquals(firstQuote.hashCode(), secondQuote.hashCode(), "Those quotes hash code are different!");
	}

}
