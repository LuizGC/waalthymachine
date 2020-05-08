package com.wealthy.machine.bovespa.sharecode;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.wealthy.machine.core.sharecode.ShareCode;

import java.io.IOException;

public class BovespaShareCodeSerializer extends StdSerializer<ShareCode> {

	public BovespaShareCodeSerializer() {
		super(ShareCode.class);
	}

	@Override
	public void serialize(ShareCode shareCode, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(shareCode.getCode());
	}
}
