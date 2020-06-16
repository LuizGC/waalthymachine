package com.wealthy.machine.core.util.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.core.util.DataFileGetter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonDataFileHandlerTest {

	private DataFileGetter fileGetter;
	private JsonDataFileHandler jsonDataFile;

	@BeforeEach
	public void setup() {
		this.fileGetter = mock(DataFileGetter.class);
		this.jsonDataFile = new JsonDataFileHandler(fileGetter);
	}

	@AfterEach
	public void tearDown() {
		this.jsonDataFile = null;
	}

	@Test
	public void list_EmptyFile_ShouldReturnEmptyList() throws IOException {
		var key = "list_EmptyFile_ShouldReturnEmptyList";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		var shouldBeEmptyList = jsonDataFile.list("list_EmptyFile_ShouldReturnEmptyList", String.class);
		assertTrue(shouldBeEmptyList.isEmpty());
	}

	@Test
	public void save_EmptyFile_ShouldReturnEmptyList() throws IOException {
		var key = "save_EmptyFile_ShouldReturnEmptyList";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		jsonDataFile.append(key, Set.of("b", "c", "a"), String.class);
		var fileWithData = fileGetter.getFile(key);
		var fileData = Files.readString(fileWithData.toPath());
		assertEquals("[\"a\",\"b\",\"c\"]", fileData);
	}

	@Test
	public void save_PassingModule_ShouldUsingSerializer() throws IOException {
		var key = "save_PassingModule_ShouldUsingSerializer";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		var serializer = getJsonSerializerMock();
		var module = new SimpleModule();
		module.addSerializer(String.class, serializer);
		jsonDataFile.append(key, Set.of("b", "c", "a"), String.class, module);
		verify(serializer, times(3))
				.serialize(anyString(), any(JsonGenerator.class), any(SerializerProvider.class));
	}

	private JsonSerializer<String> getJsonSerializerMock() {
		return (JsonSerializer<String>) mock(JsonSerializer.class);
	}

	@Test
	public void save_SaveManyDifferenceSetWithSomeRepeatedData_ShouldSaveWithoutRepeat() throws IOException {
		var key = "save_PassingModule_ShouldUsingSerializer";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		jsonDataFile.append(key, Set.of("b", "c", "a"), String.class);
		jsonDataFile.append(key, Set.of("g", "t", "r"), String.class);
		jsonDataFile.append(key, Set.of("a", "r", "s", "test"), String.class);
		jsonDataFile.append(key, Set.of("test1", "test2", "test3", "test4"), String.class);
		jsonDataFile.append(key, Set.of("b", "a", "d", "c"), String.class);
		var expectedResult = "[\"a\",\"b\",\"c\",\"d\",\"g\",\"r\",\"s\",\"t\",\"test\",\"test1\",\"test2\",\"test3\",\"test4\"]";
		var fileText = Files.readString(testFile);
		assertEquals(expectedResult, fileText);
	}

	@Test
	public void list_WhenTheFileIsNotEmpty_ShouldReturnData() throws IOException {
		var key = "list_WhenTheFileIsNotEmpty_ShouldReturnData";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		IOUtil.writeText("[\"a\",\"b\",\"c\"]", testFile.toFile());
		var shouldNotBeEmptyList = jsonDataFile.list(key, String.class);
		assertFalse(shouldNotBeEmptyList.isEmpty());
	}

	@Test
	public void save_ProblemWithFile_ShouldThrowException() throws IOException {
		var key = "save_EmptyFile_ShouldReturnEmptyList";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		var serializer = getJsonSerializerMock();
		doThrow(new IOException()).when(serializer)
				.serialize(anyString(), any(JsonGenerator.class), any(SerializerProvider.class));
		var module = new SimpleModule();
		module.addSerializer(String.class, serializer);
		assertThrows(RuntimeException.class, () -> jsonDataFile.append(key, Set.of("b", "c", "a"), String.class, module));
	}

	@Test
	public void list_ProblemWithFile_ShouldThrowException() throws IOException {
		var key = "list_ProblemWithFile_ShouldThrowException";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		IOUtil.writeText("is not an array", testFile.toFile());
		assertThrows(RuntimeException.class, () ->
				jsonDataFile.list(key, String.class)
		);
	}

	@Test
	public void override_TwoDifferentSave_ShouldOverrideData() throws IOException {
		var key = "override_TwoDifferentSave_ShouldOverrideData";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		jsonDataFile.override(key, Set.of("b", "c", "a"));
		jsonDataFile.override(key, Set.of("x", "t", "r"), null);
		var expectedResult = "[\"r\",\"t\",\"x\"]";
		var fileText = Files.readString(testFile);
		assertEquals(expectedResult, fileText);
	}

}