package com.wealthy.machine.core.util.data;

import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.util.DataFileGetter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class JsonDataFileHandlerTest {

	@Test
	public void listObject_EmptyFile_ShouldReturnEmptyList() throws IOException {
		var file = Files.createTempDirectory("listObject_EmptyFile_ShouldReturnEmptyList").toFile();
		var config = new Config();
		var fileGetter = new DataFileGetter(file, config);
		var jsonDataFile = new JsonDataFileHandler(fileGetter, config);
		var shouldBeEmptyList = jsonDataFile.list("test", String.class);
		assertTrue(shouldBeEmptyList.isEmpty());
	}
}