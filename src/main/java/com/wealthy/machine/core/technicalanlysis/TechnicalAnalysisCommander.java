package com.wealthy.machine.core.technicalanlysis;

import com.wealthy.machine.core.util.technicalanlysis.type.ValueType;

import java.util.List;
import java.util.Map;

public interface TechnicalAnalysisCommander {

	Double calculate(ValueType type, List<Map<String, Double>> elements);

}
