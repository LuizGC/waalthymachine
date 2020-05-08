package com.wealthy.machine.bovespa.sharecode;

import java.util.Set;

public class BovespaShareCodeValidator {

	//List with codes of possible cash market stocks: 3 is ON, 4 is PN or 11 UNIT
	//It must works for fractional share market: 3F, 4F, 11F
	private final static Set<String> CODES_ALLOWED_CASH_MARKET = Set.of("3", "3F", "4", "4F", "11", "11F");

	private final String code;

	public BovespaShareCodeValidator(String code) {
		this.code = code;
	}

	public Boolean isCorrectSize() {
		return code.length() >= 5 && code.length() <= 7;
	}

	public Boolean isFourInitialsOnlyLetter() {
		for (int i = 0; i < 4; i++) {
			if (!Character.isLetterOrDigit(code.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public Boolean isShareCashMarketAllowed(){
		return CODES_ALLOWED_CASH_MARKET.contains(code.substring(4));
	}

}
