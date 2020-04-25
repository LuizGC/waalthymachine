package com.wealthy.machine.sharecode.bovespa;

import com.wealthy.machine.sharecode.ShareCode;

import java.util.Objects;

public class BovespaShareCode implements ShareCode {

	private final String code;

	public BovespaShareCode(String code) {
		this(code, new BovespaShareCodeValidator(code));
	}

	public BovespaShareCode(String code, BovespaShareCodeValidator validator){
		if(!validator.isCorrectSize()) {
			throw new RuntimeException(code + " is invalid share code size!");
		}
		if(!validator.isFourInitialsOnlyLetter()){
			throw new RuntimeException(code + " is not valid. The four initials characters must be letters.");
		}
		this.code = code;
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
