package com.wealthy.machine.core.technicalanlysis.bollingerband;

import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.function.BinaryOperator;

public enum BollingerBandSide {

	UPPER(WealthNumber::add), LOWER(WealthNumber::subtract);

	private final BinaryOperator<WealthNumber> operation;

	BollingerBandSide(BinaryOperator<WealthNumber> operation) {
		this.operation = operation;
	}

	public WealthNumber calculate(WealthNumber average, WealthNumber standardDeviation) {
		return this.operation.apply(average, standardDeviation);
	}
}
