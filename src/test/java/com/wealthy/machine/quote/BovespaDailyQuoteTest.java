package com.wealthy.machine.quote;

import com.wealthy.machine.util.BovespaDaileQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BovespaDailyQuoteTest {

	@Test
	public void testEqulas(){
		var random = new Random();
		var firstQuote = new BovespaDaileQuoteBuilder().build();
		var secondQuote = new BovespaDaileQuoteBuilder()
				.avgPrice(random.nextDouble())
				.company("dsadsadasdsa")
				.build();
		assertEquals(firstQuote, secondQuote, "Those quotes must be equals!");
		secondQuote = new BovespaDaileQuoteBuilder().shareCode("ABCQ1").build();
		assertNotEquals(firstQuote, secondQuote, "Those quotes are different!");
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1989);
		secondQuote = new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build();
		assertNotEquals(firstQuote, secondQuote, "Those quotes are different!");
	}

	@Test
	public void testHashCode(){
		var random = new Random();
		var firstQuote = new BovespaDaileQuoteBuilder().build();
		var secondQuote = new BovespaDaileQuoteBuilder()
				.avgPrice(random.nextDouble())
				.company("dsadsadasdsa")
				.build();
		assertEquals(firstQuote.hashCode(), secondQuote.hashCode(), "Those quotes hash code must be equals!");
		secondQuote = new BovespaDaileQuoteBuilder().shareCode("ABCQ1").build();
		assertNotEquals(firstQuote.hashCode(), secondQuote.hashCode(), "Those quotes hash code are different!");
		var calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1989);
		secondQuote = new BovespaDaileQuoteBuilder().tradingDay(calendar.getTime()).build();
		assertNotEquals(firstQuote.hashCode(), secondQuote.hashCode(), "Those quotes hash code are different!");
	}

}
