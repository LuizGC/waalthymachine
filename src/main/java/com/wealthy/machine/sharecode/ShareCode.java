package com.wealthy.machine.sharecode;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(using = ShareCodeSerializer.class)
public interface ShareCode extends Serializable {

	String getCode();

}
