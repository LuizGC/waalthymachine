package com.wealthy.machine.core;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

	public void getSecretsValueFromGitHub_GetValue_ShouldBeEquals() {
		assertEquals("works!!!", System.getenv("DOES_IT_WORKS"));
	}


}