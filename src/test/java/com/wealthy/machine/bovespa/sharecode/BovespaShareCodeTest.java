package com.wealthy.machine.bovespa.sharecode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BovespaShareCodeTest {

	@Test
	public void constructor_WhenShareCodeIsInvalid_ShouldThrowException() {
		assertThrows(RuntimeException.class, () -> new BovespaShareCode("SANB11FX"));
		assertThrows(RuntimeException.class, () -> new BovespaShareCode("SANB"));
		assertThrows(RuntimeException.class, () -> new BovespaShareCode("SAN 11F"));
	}

	@Test
	public void equals_WhenShareCodeHasTheSameCode_ShouldBeEquals() {
		var shareCodeTest = "SANB11";
		var shareCode1 = new BovespaShareCode(shareCodeTest);
		var shareCode2 = new BovespaShareCode(shareCodeTest);
		assertEquals(shareCode1, shareCode2);
	}

	@Test
	public void compareTo_WhenAlphabeticOrder_ShouldBeMinusOne() {
		var shareCode1 = new BovespaShareCode("AAAA11");
		var shareCode2 = new BovespaShareCode("BBBB5");
		assertEquals(-1, shareCode1.compareTo(shareCode2));
	}
}
