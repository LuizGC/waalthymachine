package com.wealthy.machine.sharecode;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ShareCodeSerializer extends StdSerializer<ShareCode> {

	public ShareCodeSerializer() {
		super(ShareCode.class);
	}

	@Override
	public void serialize(ShareCode shareCode, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(shareCode.getCode());
	}
}
