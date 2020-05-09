package com.wealthy.machine.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

	@Test
	public void getSecretsValueFromGitHub_GetValue_ShouldBeEquals() {
		System.out.println(System.getenv("DOES_IT_WORKS"));
		assertEquals("works!!!", System.getenv("DOES_IT_WORKS"));
	}


}