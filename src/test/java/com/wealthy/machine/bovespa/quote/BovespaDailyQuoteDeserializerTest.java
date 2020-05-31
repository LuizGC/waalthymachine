package com.wealthy.machine.bovespa.quote;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.util.number.WealthNumber;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BovespaDailyQuoteDeserializerTest {

	@Test
	void deserialize_WhenDataIsCorrect_ReturnBovespaDailyQuote() throws IOException {
		var jsonParser = mock(JsonParser.class);
		var deserializationContext = mock(DeserializationContext.class);
		var objectCodec = mock(ObjectCodec.class);
		var jsonNode = mock(JsonNode.class);
		when(jsonParser.getCodec()).thenReturn(objectCodec);
		when(objectCodec.readTree(eq(jsonParser))).thenReturn(jsonNode);
		var shareCode = new BovespaShareCode("ABCD1");
		var deserializer = new BovespaDailyQuoteDeserializer(shareCode);
		var tradingDay = new Date();
		var openPrice = new WealthNumber("1.1");
		var closePrice = new WealthNumber("2.2");
		var minPrice = new WealthNumber("3.3");
		var maxPrice = new WealthNumber("4");
		var volume = new WealthNumber("6");

		when(jsonNode.get("tradingDate")).thenReturn(new LongNode(tradingDay.getTime()));
		when(jsonNode.get("openPrice")).thenReturn(new TextNode(openPrice.toString()));
		when(jsonNode.get("closePrice")).thenReturn(new TextNode(closePrice.toString()));
		when(jsonNode.get("minPrice")).thenReturn(new TextNode(minPrice.toString()));
		when(jsonNode.get("maxPrice")).thenReturn(new TextNode(maxPrice.toString()));
		when(jsonNode.get("volume")).thenReturn(new TextNode(volume.toString()));

		var dailyQuote = deserializer.deserialize(jsonParser, deserializationContext);

		assertEquals(tradingDay, dailyQuote.getTradingDay());
		assertEquals(openPrice, dailyQuote.getOpenPrice());
		assertEquals(closePrice, dailyQuote.getClosePrice());
		assertEquals(minPrice, dailyQuote.getLowPrice());
		assertEquals(maxPrice, dailyQuote.getHighPrice());
		assertEquals(volume, dailyQuote.getVolume());
		assertEquals(shareCode, dailyQuote.getShareCode());
	}
}