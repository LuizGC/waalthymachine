package com.wealthy.machine.core.util.number;

import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class WealthNumber {

	private final BigDecimal number;

	public final static WealthNumber ZERO = new WealthNumber("0");
	public final static WealthNumber ONE = new WealthNumber("1");
	public final static WealthNumber TWO = new WealthNumber("2");

	public WealthNumber(String number) {
		this.number = new BigDecimal(number)
				.setScale(2, RoundingMode.HALF_EVEN);
	}

	public WealthNumber(int value) {
		this(String.valueOf(value));
	}

	public WealthNumber divide(WealthNumber valueOf) {
		var newNumber = this.number
				.divide(valueOf.number, RoundingMode.HALF_EVEN);
		return new WealthNumber(newNumber.toPlainString());
	}

	public WealthNumber add(WealthNumber number) {
		var newNumber = this.number.add(number.number);
		return new WealthNumber(newNumber.toPlainString());
	}

	public WealthNumber subtract(WealthNumber number) {
		var newNumber = this.number.subtract(number.number);
		return new WealthNumber(newNumber.toPlainString());
	}

	public WealthNumber multiply(WealthNumber number) {
		var newNumber = this.number.multiply(number.number);
		return new WealthNumber(newNumber.toPlainString());
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

	@Override
	public String toString() {
		return number.toPlainString();
	}

	@JsonValue
	public double doubleValue() {
		return number.doubleValue();
	}
}
