package com.wealthy.machine.core.math.number;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class WealthNumberSerializer extends StdSerializer<WealthNumber> {

	public WealthNumberSerializer() {
		super(WealthNumber.class);
	}

	@Override
	public void serialize(WealthNumber number, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeNumber(number.toString());
	}
}
