package com.wealthy.machine.bovespa.dataaccess;

import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BovespaShareCodeDataAccessTest {

	@Test
	void save_WhenShareCodeIsCorrect_ShouldCallSave() {
		var jsonDataFileHandler = mock(JsonDataFileHandler.class);
		var dataAccess = new BovespaShareCodeDataAccess(jsonDataFileHandler);
		var dataSet = Set.of(new BovespaShareCode("SANB11"));
		dataAccess.save(dataSet);
		verify(jsonDataFileHandler).save(anyString(), eq(dataSet), any(), any());
	}
}