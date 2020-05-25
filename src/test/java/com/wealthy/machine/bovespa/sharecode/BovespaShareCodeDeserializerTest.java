package com.wealthy.machine.bovespa.sharecode;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BovespaShareCodeDeserializerTest {

	@Test
	void deserialize_WhenShareCodeIsValid_ShouldReturnBovespaShareCode() throws IOException {
		var shareCode = "SANB11";
		var jsonParser = mock(JsonParser.class);
		var deserializationContext = mock(DeserializationContext.class);
		when(jsonParser.getText()).thenReturn(shareCode);;
		var shareCodeDeserializer = new BovespaShareCodeDeserializer();
		var shareCodeDeserialize = shareCodeDeserializer.deserialize(jsonParser, deserializationContext);
		assertEquals(new BovespaShareCode(shareCode), shareCodeDeserialize);
	}

}