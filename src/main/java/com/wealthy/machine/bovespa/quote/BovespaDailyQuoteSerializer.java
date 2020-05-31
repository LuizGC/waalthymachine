package com.wealthy.machine.bovespa.quote;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.wealthy.machine.core.sharecode.ShareCode;

import java.io.IOException;

public class BovespaDailyQuoteSerializer extends StdSerializer<BovespaDailyQuote> {

	public BovespaDailyQuoteSerializer() {
		super(BovespaDailyQuote.class);
	}

	@Override
	public void serialize(BovespaDailyQuote dailyQuote, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("low", dailyQuote.getLowPrice().toString());
		gen.writeStringField("high", dailyQuote.getHighPrice().toString());
		gen.writeStringField("open", dailyQuote.getOpenPrice().toString());
		gen.writeStringField("close", dailyQuote.getClosePrice().toString());
		gen.writeStringField("value", dailyQuote.getVolume().toString());
		gen.writeNumberField("time", dailyQuote.getTradingDay().getTime());
		gen.writeEndObject();
	}
}
