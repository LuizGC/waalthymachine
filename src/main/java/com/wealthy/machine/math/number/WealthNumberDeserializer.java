package com.wealthy.machine.math.number;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class WealthNumberDeserializer extends StdDeserializer<WealthNumber> {

	public WealthNumberDeserializer() {
		super(WealthNumber.class);
	}

	@Override
	public WealthNumber deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		return new WealthNumber(p.getText());
	}


}
