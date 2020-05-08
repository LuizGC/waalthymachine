package com.wealthy.machine.core.datamanager;

import com.wealthy.machine.bovespa.dataaccess.BovespaStockQuoteDataAccessLayer;
import com.wealthy.machine.bovespa.processor.BovespaDataProcessor;
import com.wealthy.machine.bovespa.seeker.BovespaDataSeeker;
import com.wealthy.machine.bovespa.util.BovespaDailyQuoteBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Set;

import static org.mockito.Mockito.*;

public class BovespaDataProcessorTest {

	@Test
	public void testCallingAllMethodsToSaveData() throws MalformedURLException {
		var url = new URL("http://www.google.com");
		var bovespaDaily = new BovespaDailyQuoteBuilder().build();
		var setBovespaDaily =  Set.of(bovespaDaily);
		var dataReader = mock(BovespaDataSeeker.class);
		var dataAccessLayer = mock(BovespaStockQuoteDataAccessLayer.class);
		var dataManager = new BovespaDataProcessor(dataReader, dataAccessLayer);

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
		new BovespaDataProcessor(folder);
	}

}
