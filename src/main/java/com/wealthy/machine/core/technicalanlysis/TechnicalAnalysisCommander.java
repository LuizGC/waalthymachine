package com.wealthy.machine.core.technicalanlysis;

import com.wealthy.machine.bovespa.quote.type.ValueType;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.util.Collection;

public interface TechnicalAnalysisCommander {

	WealthNumber calculate(ValueType type, Collection<DailyQuote> quotes);

}
