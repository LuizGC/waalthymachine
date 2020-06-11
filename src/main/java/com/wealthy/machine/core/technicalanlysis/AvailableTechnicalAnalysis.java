package com.wealthy.machine.core.technicalanlysis;


import com.wealthy.machine.core.technicalanlysis.movingaverage.SimpleMovingAverage;
import com.wealthy.machine.core.util.technicalanlysis.type.ValueType;

import java.util.List;
import java.util.Map;

public enum AvailableTechnicalAnalysis {

	SIMPLE_MOVING_AVERAGE(new SimpleMovingAverage());

	private final TechnicalAnalysisCommander analysisCommander;

	AvailableTechnicalAnalysis(TechnicalAnalysisCommander simpleMovingAverage) {
		this.analysisCommander = simpleMovingAverage;
	}

	public Double calculate(ValueType type, List<Map<String, Double>> elements) {
		return this.analysisCommander.calculate(type, elements);
	}

}
