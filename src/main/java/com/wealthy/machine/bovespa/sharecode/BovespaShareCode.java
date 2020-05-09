package com.wealthy.machine.bovespa.sharecode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.sharecode.ShareCode;
import org.slf4j.Logger;

import java.util.Objects;

@JsonSerialize(using = BovespaShareCodeSerializer.class)
@JsonDeserialize(using = BovespaShareCodeDeserializer.class)
public class BovespaShareCode implements ShareCode {

	private final String code;
	private final Logger logger;

	public BovespaShareCode(String code) {
		this(code, new BovespaShareCodeValidator(code));
	}

	public BovespaShareCode(String code, BovespaShareCodeValidator validator){
		this.logger = new Config().getLogger(this.getClass());
		if(!validator.isCorrectSize()) {
			this.logger.error(code + " is invalid share code size!");
			throw new RuntimeException();
		}
		if(!validator.isFourInitialsOnlyLetter()) {
			this.logger.error(code + " is not valid. The four initials characters must be letters.");
			throw new RuntimeException();
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
		return Objects.hash(this.getClass().getName(), getCode());
	}

	@Override
	public int compareTo(ShareCode shareCode) {
		return this.getCode().compareTo(shareCode.getCode());
	}

}
