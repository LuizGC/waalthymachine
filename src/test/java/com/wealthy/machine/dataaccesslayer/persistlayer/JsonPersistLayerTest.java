package com.wealthy.machine.dataaccesslayer.persistlayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPersistLayerTest {

	private static class TestObject {

		private String text;

		@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
		public TestObject(@JsonProperty("text") String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			TestObject that = (TestObject) o;
			return getText().equals(that.getText());
		}

		@Override
		public int hashCode() {
			return Objects.hash(getText());
		}
	}

	private final Config config = new Config();
	private final TypeReference<Set<TestObject>> typeReference = new TypeReference<>() {};

	@Test
	public void testSavingSerializableData() throws IOException {
		var folder = Files.createTempDirectory("testSavingSerializableData").toFile();
		var persistLayer = new JsonPersistLayer<>(folder, typeReference);
		var saveSet = Set.of(new TestObject("testSavingSerializableData"));
		var saveSetJson = new ObjectMapper().writeValueAsString(saveSet);
		var key = new Object().toString();
		persistLayer.saveData(key, saveSet);
		var keyFolder = new File(folder, key);
		var filePath = new File(keyFolder, config.getDefaultFilename()).toPath();
		var fileData = new String(Files.readAllBytes(filePath));
		assertEquals(saveSetJson, fileData);
	}

	@Test
	public void testReadingSerializableData() throws IOException {
		var folder = Files.createTempDirectory("testSavingSerializableData").toFile();
		var persistLayer = new JsonPersistLayer<>(folder, typeReference);
		var saveSet = Set.of(new TestObject("testReadingSerializableData"));
		var saveSetJson = new ObjectMapper().writeValueAsString(saveSet);
		var key = new Object().toString();
		var keyFolder = new File(folder, key);
		keyFolder.mkdirs();
		var file = new File(keyFolder, config.getDefaultFilename());
		file.createNewFile();
		Files.write(file.toPath(), saveSetJson.getBytes());
		var fileData = persistLayer.readFileContent(key);
		assertFalse(fileData.isEmpty());
		assertEquals(1, fileData.size());
		assertTrue(fileData.containsAll(saveSet));
	}

}
