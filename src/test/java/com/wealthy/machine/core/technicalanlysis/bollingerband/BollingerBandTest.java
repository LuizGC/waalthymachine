package com.wealthy.machine.core.technicalanlysis.bollingerband;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.util.number.WealthNumber;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BollingerBandTest {

	@Test
	void calculate_WhenHappyPath_ShouldReturnCorrectValue() {
		var openPriceList = List.of(9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4);
		var calendar = Calendar.getInstance();
		var dailyQuotes = new HashSet<DailyQuote>();
		var builder = new BovespaDailyQuoteBuilder();
		for (int openPrice : openPriceList) {
			var quote = builder
					.tradingDay(calendar.getTime())
					.openPrice(String.valueOf(openPrice))
					.build();
			dailyQuotes.add(quote);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		var bollinger = new BollingerBand(BollingerBandSide.UPPER, 2);
		var upperBand = bollinger.calculate(ValueType.OPEN, dailyQuotes);
		assertEquals(new WealthNumber("12.96"), upperBand);
		bollinger = new BollingerBand(BollingerBandSide.LOWER, 2);
		var lowerBand = bollinger.calculate(ValueType.OPEN, dailyQuotes);
		assertEquals(new WealthNumber("1.04"), lowerBand);
	}

	@Test
	void calculate_WhenCollectionIsEmpty_ShouldReturnZero() {
		var dailyQuotes = new HashSet<DailyQuote>();
		var bollinger = new BollingerBand(BollingerBandSide.UPPER, 2);
		var upperBand = bollinger.calculate(ValueType.OPEN, dailyQuotes);
		assertEquals(WealthNumber.ZERO, upperBand);
		bollinger = new BollingerBand(BollingerBandSide.LOWER, 2);
		var lowerBand = bollinger.calculate(ValueType.OPEN, dailyQuotes);
		assertEquals(WealthNumber.ZERO, lowerBand);
	}

}