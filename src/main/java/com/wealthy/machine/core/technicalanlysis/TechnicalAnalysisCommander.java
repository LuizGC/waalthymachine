package com.wealthy.machine.core.technicalanlysis;

import com.wealthy.machine.core.util.technicalanlysis.type.ValueType;

import java.util.List;
import java.util.Map;

public interface TechnicalAnalysisCommander {

	String calculate(ValueType type, List<Map<String, String>> elements);

	String createName(ValueType valueType, Integer primeNumber);

}
