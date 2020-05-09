package com.wealthy.machine.core.util.number;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wealthy.machine.core.Config;

import java.math.BigDecimal;
import java.util.Objects;

@JsonSerialize(using = WealthNumberSerializer.class)
public class WealthNumber {

	private BigDecimal number;

	public WealthNumber(String number) {
		var config = new Config();
		this.number = new BigDecimal(number, config.getMathContext())
				.setScale(config.getNumberScale(), config.getRoundingMode());
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
