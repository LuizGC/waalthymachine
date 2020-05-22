package com.wealthy.machine.core.util.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WealthNumberTest {

	@Test
	public void toString_TestReturnFullNumber_ShouldReturnTheNumberWithoutHideDigits() {
		var textNumber = "1.78659834765936793876571236598763478658239756148756346598346597346198918764832548267";
		var number = new WealthNumber(textNumber);
		assertEquals("1.79", number.toString());
	}

	@Test
	public void equals_TestManyEqualsSituations_ShouldReturnTrueAlways() {
		assertEquals(new WealthNumber("1.78"), new WealthNumber("1.785"));
		assertEquals(new WealthNumber("99.6"), new WealthNumber("99.60"));
		assertEquals(new WealthNumber("100.00"), new WealthNumber("100"));
		assertEquals(new WealthNumber("49.999"), new WealthNumber("50"));
	}

	@Test
	public void hashCode_TestManyEqualsSituations_ShouldReturnTrueAlways() {
		assertEquals(new WealthNumber("1.78").hashCode(), new WealthNumber("1.785").hashCode());
		assertEquals(new WealthNumber("99.6").hashCode(), new WealthNumber("99.60").hashCode());
		assertEquals(new WealthNumber("100.00").hashCode(), new WealthNumber("100").hashCode());
		assertEquals(new WealthNumber("49.999").hashCode(), new WealthNumber("50").hashCode());
	}
}