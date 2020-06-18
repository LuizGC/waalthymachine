package com.wealthy.machine.core.util.collection;

import com.wealthy.machine.core.util.DataFileGetter;
import com.wealthy.machine.core.util.data.JsonDataFileHandler;
import com.wealthy.machine.core.util.number.WealthNumber;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparatorListAdapterTest {

	@Test
	void serialize_WhenArrayWealthNumber_ShouldSaveArrayDouble() throws IOException {
		var key = "serialize_WhenArrayWealthNumber_ShouldSaveArrayDouble";
		var path = Files.createTempDirectory(key);
		var fileGetter = new DataFileGetter(path.toFile());
		var jsonFileHandler = new JsonDataFileHandler(fileGetter);
		var list = new ArrayList<ComparatorListAdapter<WealthNumber>>();
		list.add(new ComparatorListAdapter<>(List.of(new WealthNumber("30"), new WealthNumber("28")), 1));
		list.add(new ComparatorListAdapter<>(List.of(new WealthNumber("50"), new WealthNumber("12")), 2));
		jsonFileHandler.override(key, list);
		var fileText = Files.readString(fileGetter.getFile(key).toPath());
		assertEquals("[[30.0,28.0],[50.0,12.0]]", fileText);
	}
}