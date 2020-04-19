package com.wealthy.machine.sharecode;

import java.util.Objects;

public class BovespaShareCode implements ShareCode{

	private final String code;

	private boolean isOnlyLetter(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isLetter(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public BovespaShareCode(String code) {
		if(code.length() < 5  || code.length() > 7  ) {
			throw new RuntimeException(code + " is invalid share code size!");
		}
		if(!isOnlyLetter(code.substring(0, 4))){
			throw new RuntimeException(code + "is not valid. The four initials characters must be letters.");
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
