package com.wealthy.machine.bovespa.dataaccess;

import com.wealthy.machine.core.util.data.JsonDataFileHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BovespaUrlDataAccessTest {

	private JsonDataFileHandler jsonDataFileHandler;
	private static final String URL_FORMAT = "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP";
	private static final int INITIAL_YEAR = 2010;

	@BeforeEach
	void setUp() {
		this.jsonDataFileHandler = mock(JsonDataFileHandler.class);
	}

	@Test
	void save_WhenTheUrlsIsRight_SaveSuccessfully() throws MalformedURLException {
		var urlDataAccess = new BovespaUrlDataAccess(this.jsonDataFileHandler, INITIAL_YEAR, URL_FORMAT);
		var set = Set.of(new URL(URL_FORMAT));
		urlDataAccess.save(set);
		ArgumentCaptor<Set<String>> argument = ArgumentCaptor.forClass(Set.class);
		verify(this.jsonDataFileHandler).save(anyString(), argument.capture(), eq(String.class));
		assertIterableEquals(Set.of(URL_FORMAT), argument.getValue());
	}

	@Test
	void listMissingUrl_WhenThereAreDataAlreadySaved_ShouldReturnListCorrect() {
		var savedUrls = Set.of(
			URL_FORMAT.replace("{{YYYY}}", "2015"),
			URL_FORMAT.replace("{{YYYY}}", "2016"),
			URL_FORMAT.replace("{{YYYY}}", "2017")
		);
		when(jsonDataFileHandler.list(anyString(),eq(String.class))).thenReturn(savedUrls);
		var urlDataAccess = new BovespaUrlDataAccess(this.jsonDataFileHandler, INITIAL_YEAR, URL_FORMAT);
		urlDataAccess
				.listMissingUrl()
				.stream()
				.map(URL::toString)
				.forEach(url -> assertFalse(savedUrls.contains(url)));
	}

	@Test
	void listMissingUrl_WhenTheDefaultUrlIsInvalid_ShouldThrowException() {
		when(jsonDataFileHandler.list(anyString(),eq(String.class))).thenReturn(Set.of(""));
		var urlDataAccess = new BovespaUrlDataAccess(this.jsonDataFileHandler, 2010, "invalid Url");
		assertThrows(RuntimeException.class, urlDataAccess::listMissingUrl);
	}

}