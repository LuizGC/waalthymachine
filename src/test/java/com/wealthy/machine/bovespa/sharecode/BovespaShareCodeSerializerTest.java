package com.wealthy.machine.bovespa.sharecode;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class BovespaShareCodeSerializerTest {

	@Test
	void serialize_WhenSerialize_WriteShareCode() throws IOException {
		var serializer = new BovespaShareCodeSerializer();
		var shareCode = new BovespaShareCode("SANB11");
		var jsonGenerator = mock(JsonGenerator.class);
		var serializerProvider = mock(SerializerProvider.class);
		serializer.serialize(shareCode, jsonGenerator, serializerProvider);
		verify(jsonGenerator, times(1)).writeString(shareCode.getCode());
	}

}