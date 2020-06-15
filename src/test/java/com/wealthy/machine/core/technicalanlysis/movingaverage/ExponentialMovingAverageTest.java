package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import com.wealthy.machine.core.util.number.WealthNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExponentialMovingAverageTest {

	private ExponentialMovingAverage movingAverage;

	private static Stream<Arguments> getDailyData() {
		return Stream.of(
				Arguments.of(2, 4, 6, 8, 6.25),
				Arguments.of(11.11, 16.16, 18.18, 20.20, 18.06),
				Arguments.of(14, 16, 18, 20, 18.25)
		);
	}

	@BeforeEach
	void setUp() {
		this.movingAverage = new ExponentialMovingAverage();
	}

	@Test
	void calculate_WhenDailyQuoteListIsEmpty_ShouldReturnZero() {
		WealthNumber movingAverageValue = this.movingAverage.calculate(ValueType.OPEN, Collections.emptyList());
		assertEquals(WealthNumber.ZERO, movingAverageValue);
	}

	@Test
	void calculate_WhenDailyQuoteListHasOnlyOneElement_ShouldReturnTheOpenValueElement() {
		var testedValue = "33.98";
		var quote = new BovespaDailyQuoteBuilder()
				.openPrice(testedValue)
				.build();
		WealthNumber movingAverageValue = this.movingAverage.calculate(ValueType.OPEN, List.of(quote));
		assertEquals(new WealthNumber(testedValue), movingAverageValue);
	}

	@Test
	void calculate_WhenDailyQuoteListHasOnlyTwoElement_ShouldReturnTheAverageOfOpenValueElement() {
		var builder = new BovespaDailyQuoteBuilder();
		var calendar = Calendar.getInstance();
		var quote1 = builder
				.tradingDay(calendar.getTime())
				.openPrice("3")
				.build();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		var quote2 = builder
				.tradingDay(calendar.getTime())
				.openPrice("5")
				.build();
		WealthNumber movingAverageValue = this.movingAverage.calculate(ValueType.OPEN, List.of(quote1, quote2));
		assertEquals(new WealthNumber("4"), movingAverageValue);
	}

	@ParameterizedTest
	@MethodSource("getDailyData")
	void calculate_WhenDailyQuoteList_ShouldReturnDailyCorrectValue(double d1, double d2, double d3, double d4, double result) {
		var builder = new BovespaDailyQuoteBuilder();
		var calendar = Calendar.getInstance();
		var quote1 = builder
				.tradingDay(calendar.getTime())
				.openPrice(String.valueOf(d1))
				.build();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		var quote2 = builder
				.tradingDay(calendar.getTime())
				.openPrice(String.valueOf(d2))
				.build();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		var quote3 = builder
				.tradingDay(calendar.getTime())
				.openPrice(String.valueOf(d3))
				.build();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		var quote4 = builder
				.tradingDay(calendar.getTime())
				.openPrice(String.valueOf(d4))
				.build();
		WealthNumber movingAverageValue = this.movingAverage.calculate(ValueType.OPEN, List.of(
				quote1,
				quote2,
				quote3,
				quote4
		));
		assertEquals(new WealthNumber(String.valueOf(result)), movingAverageValue);
	}
}