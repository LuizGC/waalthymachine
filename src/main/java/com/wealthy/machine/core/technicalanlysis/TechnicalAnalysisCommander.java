package com.wealthy.machine.core.technicalanlysis;

import com.wealthy.machine.bovespa.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.List;

public interface TechnicalAnalysisCommander {

	WealthNumber calculate(ValueType type, List<DailyQuote> quotes);

}
