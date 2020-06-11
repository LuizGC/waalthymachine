package com.wealthy.machine.core.technicalanlysis.movingaverage;

import com.wealthy.machine.core.technicalanlysis.TechnicalAnalysisCommander;
import com.wealthy.machine.core.util.technicalanlysis.type.ValueType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class SimpleMovingAverage implements TechnicalAnalysisCommander {

	public Double calculate(final ValueType type, List<Map<String, Double>> elements) {
		return elements
				.stream()
				.map(e -> BigDecimal.valueOf(e.get(type.getJsonFileAccessKey())))
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(elements.size()), RoundingMode.HALF_EVEN)
				.setScale(2, RoundingMode.HALF_EVEN)
				.doubleValue();
	}
}
