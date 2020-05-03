package com.wealthy.machine.datamanager;

import com.wealthy.machine.dataaccesslayer.bovespa.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.reader.BovespaDataReader;
import com.wealthy.machine.util.BovespaDailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Set;

import static org.mockito.Mockito.*;

public class BovespaDataManagerTest {

	@Test
	public void testCallingAllMethodsToSaveData() throws MalformedURLException {
		var url = new URL("http://www.google.com");
		var bovespaDaily = new BovespaDailyQuoteBuilder().build();
		var setBovespaDaily =  Set.of(bovespaDaily);
		var dataReader = mock(BovespaDataReader.class);
		var dataAccessLayer = mock(BovespaStockQuoteDataAccessLayer.class);
		var dataManager = new BovespaDataManager(dataReader, dataAccessLayer);

		when(dataReader.read(url)).thenReturn(setBovespaDaily);
		when(dataAccessLayer.listUnsavedPaths()).thenReturn(Set.of(url));

		dataManager.getMissingData();

		verify(dataReader, times(1)).read(url);
		verify(dataAccessLayer, times(1)).listUnsavedPaths();
		verify(dataAccessLayer, times(1)).save(setBovespaDaily);
	}

	@Test
	public void shouldCreateBovespaDataManager() throws IOException {
		var folder = Files.createTempDirectory("shouldCreateBovespaDataManager").toFile();
		new BovespaDataManager(folder);
	}

}
