package com.wealthy.machine.core.util.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wealthy.machine.core.Config;
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
		var config = mock(Config.class);
		this.fileGetter = mock(DataFileGetter.class);
		this.jsonDataFile = new JsonDataFileHandler(fileGetter, config);
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
		jsonDataFile.save(key, Set.of("b", "c", "a"), String.class);
		var fileWithData = fileGetter.getFile(key);
		var fileData = Files.readString(fileWithData.toPath());
		assertEquals("[\"a\",\"b\",\"c\"]", fileData);
	}

	@Test
	public void save_PassingModule_ShouldUsingSerializer() throws IOException {
		var key = "save_PassingModule_ShouldUsingSerializer";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		var serializer = (JsonSerializer<String>) mock(JsonSerializer.class);
		var module = new SimpleModule();
		module.addSerializer(String.class, serializer);
		jsonDataFile.save(key, Set.of("b", "c", "a"), String.class, module);
		verify(serializer, times(3))
				.serialize(any(), any(JsonGenerator.class), any(SerializerProvider.class));
	}

	@Test
	public void save_SaveManyDifferentsSetWithSomeRepeatedData_ShouldSaveWithoutRepeat() throws IOException {
		var key = "save_PassingModule_ShouldUsingSerializer";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		jsonDataFile.save(key, Set.of("b", "c", "a"), String.class);
		jsonDataFile.save(key, Set.of("g", "t", "r"), String.class);
		jsonDataFile.save(key, Set.of("a", "r", "s", "test"), String.class);
		jsonDataFile.save(key, Set.of("test1", "test2", "test3", "test4"), String.class);
		jsonDataFile.save(key, Set.of("b", "a", "d", "c"), String.class);
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
	public void save_ProblemWithFile_ShouldThrowException() {
		var key = "save_EmptyFile_ShouldReturnEmptyList";
		when(this.fileGetter.getFile(key)).thenReturn(null);
		assertThrows(RuntimeException.class, () -> {
			jsonDataFile.save(key, Set.of("b", "c", "a"), String.class);
		});
	}

	@Test
	public void list_ProblemWithFile_ShouldThrowException() throws IOException {
		var key = "list_ProblemWithFile_ShouldThrowException";
		var testFile = Files.createTempFile(key, ".json");
		when(this.fileGetter.getFile(key)).thenReturn(testFile.toFile());
		IOUtil.writeText("is not an array", testFile.toFile());
		assertThrows(RuntimeException.class, () -> jsonDataFile.list(key, String.class));
	}

}