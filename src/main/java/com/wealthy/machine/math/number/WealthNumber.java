package com.wealthy.machine.math.number;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

@JsonSerialize(using = WealthNumberSerializer.class)
@JsonDeserialize(using = WealthNumberDeserializer.class)
public class WealthNumber {

	private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
	private static final Integer SCALE = 2;
	private static final RoundingMode HALF_EVEN = RoundingMode.HALF_EVEN;

	private BigDecimal number;

	public WealthNumber(String number) {
		this.number = new BigDecimal(number, MATH_CONTEXT)
		.setScale(SCALE, HALF_EVEN);
	}

	@Override
	public String toString() {
		return number.toPlainString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WealthNumber that = (WealthNumber) o;
		return number.equals(that.number);
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

}
