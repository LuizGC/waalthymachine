package com.wealthy.machine.sharecode;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ShareCodeSerializer.class)
public interface ShareCode {

	String getCode();

}
