package com.wealthy.machine.sharecode;

import java.util.Objects;

public class BovespaShareCode implements ShareCode{

	private final String code;

	public BovespaShareCode(String code) {
		if(code.length() < 5 ) {
			throw new RuntimeException(code + " is invalid share code");
		}
		this.code = code;
	}

	public String getType() {
		return this.code.substring(4);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BovespaShareCode that = (BovespaShareCode) o;
		return getCode().equals(that.getCode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCode());
	}
}
