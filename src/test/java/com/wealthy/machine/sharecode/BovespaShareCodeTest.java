package com.wealthy.machine.sharecode;

import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BovespaShareCodeTest {

	@Test
	public void testShareCodeLengthIsRightSize() {
		assertThrows(RuntimeException.class, () -> new BovespaShareCode("SANB11FX"));
		assertThrows(RuntimeException.class, () -> new BovespaShareCode("SANB"));
	}

	@Test
	public void testShareCodeHasOnlyAlphaNumericBetweenFourCharacters() {
		var shareCodeTest = "SANB11";
		var shareCode = new BovespaShareCode(shareCodeTest);
		assertEquals(shareCode.getCode(), shareCodeTest);
		shareCodeTest = "SANB11F";
		shareCode = new BovespaShareCode(shareCodeTest);
		assertEquals(shareCode.getCode(), shareCodeTest);
		assertThrows(RuntimeException.class, () -> new BovespaShareCode("SAN 11F"));
	}

}
