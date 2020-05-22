package com.wealthy.machine.core.util.number;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WealthNumberSerializerTest {

	@Test
	void serialize_CallWriteNumber_ShouldCallJsonGeneratorWriteNumber() throws IOException {
		var serializer = new WealthNumberSerializer();
		var number = mock(WealthNumber.class);
		var jsonGenerator = mock(JsonGenerator.class);
		var serializerProvider = mock(SerializerProvider.class);
		serializer.serialize(number, jsonGenerator, serializerProvider);
		when(number.toString()).thenReturn("42");
		verify(jsonGenerator, times(1)).writeNumber(anyString());
	}

}