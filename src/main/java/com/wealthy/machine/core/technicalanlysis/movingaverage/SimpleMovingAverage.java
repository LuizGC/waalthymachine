package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.core.technicalanlysis.TechnicalAnalysisCommander;
import com.wealthy.machine.core.util.technicalanlysis.type.ValueType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class SimpleMovingAverage implements TechnicalAnalysisCommander {

	public String calculate(final ValueType type, List<Map<String, String>> elements) {
		return elements
				.stream()
				.map(e -> new BigDecimal(e.get(type.getJsonFileAccessKey())))
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(elements.size()), RoundingMode.HALF_EVEN)
				.setScale(2, RoundingMode.HALF_EVEN)
				.toPlainString();
	}

	@Override
	public String createName(ValueType valueType, Integer primeNumber) {
		return valueType + " Simple Moving Average " + primeNumber;
	}
}
