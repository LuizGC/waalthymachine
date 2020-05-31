package com.wealthy.machine.bovespa.quote;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.util.number.WealthNumber;

import java.io.IOException;
import java.util.Date;

public class BovespaDailyQuoteDeserializer extends StdDeserializer<BovespaDailyQuote> {

	private final BovespaShareCode shareCode;

	public BovespaDailyQuoteDeserializer(BovespaShareCode shareCode) {
		super(BovespaDailyQuote.class);
		this.shareCode = shareCode;
	}

	@Override
	public BovespaDailyQuote deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode node = p.getCodec().readTree(p);
		var tradingDate = new Date(node.get("time").asLong());
		var openPrice = node.get("open").asText();
		var closePrice = node.get("close").asText();
		var minPrice = node.get("low").asText();
		var maxPrice = node.get("high").asText();
		var volume = node.get("value").asText();
		return new BovespaDailyQuote(
				tradingDate,
				shareCode,
				new WealthNumber(openPrice),
				new WealthNumber(closePrice),
				new WealthNumber(minPrice),
				new WealthNumber(maxPrice),
				new WealthNumber(volume)
		);
	}

}
