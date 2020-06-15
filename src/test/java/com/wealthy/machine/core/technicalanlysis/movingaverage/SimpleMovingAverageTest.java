package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import com.wealthy.machine.core.util.number.WealthNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleMovingAverageTest {

	private SimpleMovingAverage movingAverage;

	@BeforeEach
	void setUp() {
		this.movingAverage = new SimpleMovingAverage();
	}

	@Test
	void calculate_WhenDailyQuoteListIsEmpty_ShouldReturnZero() {
		WealthNumber movingAverageValue = this.movingAverage.calculate(ValueType.OPEN, Collections.emptyList());
		assertEquals(WealthNumber.ZERO, movingAverageValue);
	}

	@Test
	void calculate_WhenDailyQuoteListHasOnlyOneElement_ShouldReturnDailyQuoteOpenValue() {
		var testedValue = "33.98";
		var quote = new BovespaDailyQuoteBuilder()
				.openPrice(testedValue)
				.build();
		WealthNumber movingAverageValue = this.movingAverage.calculate(ValueType.OPEN, Set.of(quote));
		assertEquals(new WealthNumber(testedValue), movingAverageValue);
	}
}