package com.wealthy.machine.core.util.number;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@JsonSerialize(using = WealthNumberSerializer.class)
public class WealthNumber {

	private final BigDecimal number;

	public WealthNumber(String number) {
		this.number = new BigDecimal(number)
				.setScale(2, RoundingMode.HALF_EVEN);
	}

	public WealthNumber(int value) {
		this(String.valueOf(value));
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

	public WealthNumber add(WealthNumber number) {
		var newNumber = this.number.add(number.number);
		return new WealthNumber(newNumber.toPlainString());
	}

	public WealthNumber divide(WealthNumber valueOf) {
		var newNumber = this.number
				.divide(valueOf.number, RoundingMode.HALF_EVEN)
				.setScale(2, RoundingMode.HALF_EVEN);
		return new WealthNumber(newNumber.toPlainString());
	}
}
