package com.wealthy.machine;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.NumberFormat;

public class BigDecimalFactory {

	private final BigInteger unscaledVal;

	private final static NumberFormat numberFormatter = NumberFormat.getInstance();
	static {
		numberFormatter.setGroupingUsed(false);
		numberFormatter.setMinimumFractionDigits(2);
		numberFormatter.setMaximumFractionDigits(2);
	}

	public BigDecimalFactory() {
		this(0.0);
	}

	public BigDecimalFactory(Double value) {
		var text = numberFormatter.format(value);
		text = cleanNumber(text);
		this.unscaledVal = new BigInteger(text);
	}

	public BigDecimalFactory(String text) {
		text = cleanNumber(text);
		this.unscaledVal = new BigInteger(text);
	}

	public BigDecimalFactory(Long value) {
		this.unscaledVal = new BigInteger(value.toString());
	}

	private String cleanNumber(String text) {
		return text.replaceAll("\\.|,", "");
	}

	public BigDecimal newInstance () {
		return new BigDecimal(this.unscaledVal, 2, MathContext.DECIMAL32);
	}


}
