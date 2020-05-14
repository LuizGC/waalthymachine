package com.wealthy.machine.core.util.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.util.DataFileGetter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonDataFileHandlerTest {

	private final DataFileGetter fileGetter;
	private JsonDataFileHandler jsonDataFile;

	public JsonDataFileHandlerTest() throws IOException {
		var file = Files.createTempDirectory("listObject_EmptyFile_ShouldReturnEmptyList").toFile();
		var config = new Config();
		this.fileGetter = new DataFileGetter(file, config);
		this.jsonDataFile = new JsonDataFileHandler(fileGetter, config);
	}

	@AfterEach
	public void tearDown() {
		this.jsonDataFile = null;
	}

	@Test
	public void list_EmptyFile_ShouldReturnEmptyList() {
		var shouldBeEmptyList = jsonDataFile.list("list_EmptyFile_ShouldReturnEmptyList", String.class);
		assertTrue(shouldBeEmptyList.isEmpty());
	}

	@Test
	public void save_EmptyFile_ShouldReturnEmptyList() throws IOException {
		var key = "save_EmptyFile_ShouldReturnEmptyList";
		jsonDataFile.save(key, Set.of("b", "c", "a"), String.class);
		var fileWithData = fileGetter.getFile(key);
		var fileData = Files.readString(fileWithData.toPath());
		assertEquals("[\"a\",\"b\",\"c\"]", fileData);
	}

	@Test
	public void save_PassingModule_ShouldUsingSerializer() throws IOException {
		var key = "save_PassingModule_ShouldUsingSerializer";
		var serializer = mock(StdSerializer.class);
		var module = new SimpleModule();
		module.addSerializer(String.class, serializer);
		jsonDataFile.save(key, Set.of("b", "c", "a"), String.class, module);
		verify(serializer, times(3))
				.serialize(anyString(), any(JsonGenerator.class), any(SerializerProvider.class));
	}

	@Test
	public void list_WhenTheFileIsNotEmpty_ShouldReturnData() {
		var key = "list_WhenTheFileIsNotEmpty_ShouldReturnData";
		var fileWithData = fileGetter.getFile(key);
		IOUtil.writeText("[\"a\",\"b\",\"c\"]", fileWithData);
		var shouldNotBeEmptyList = jsonDataFile.list(key, String.class);
		assertFalse(shouldNotBeEmptyList.isEmpty());
	}

	@Test
	public void save_ProblemWithFile_ShouldThrowException() {
		var key = "save_EmptyFile_ShouldReturnEmptyList";
		var fileWithData = fileGetter.getFile(key);
		fileWithData.delete();
		fileWithData.mkdir();
		assertThrows(RuntimeException.class, () -> {
			jsonDataFile.save(key, Set.of("b", "c", "a"), String.class);
		});
	}

	@Test
	public void list_ProblemWithFile_ShouldThrowException() {
		var key = "list_ProblemWithFile_ShouldThrowException";
		var fileWithData = fileGetter.getFile(key);
		fileWithData.setReadable(false, false);
		IOUtil.writeText("[\"a\",\"b\",\"c\"]", fileWithData);
		try(
				var randomAccessFile = new RandomAccessFile(fileWithData, "rw");
				var randomAccessFileChannel = randomAccessFile.getChannel()
		) {
			randomAccessFileChannel.lock();
			assertThrows(RuntimeException.class, () -> jsonDataFile.list(key, String.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}