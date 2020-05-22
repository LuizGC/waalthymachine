package com.wealthy.machine.core.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class DataFileGetterTest {

	private DataFileGetter dataFileGetter;
	private File file;

	@BeforeEach
	void setUp() throws IOException {
		this.file = Files.createTempDirectory("DataFileGetterTest").toFile();
		this.dataFileGetter = new DataFileGetter(file);
	}

	@AfterEach
	public void tearDown() {
		this.dataFileGetter = null;
	}

	@Test
	public void getFile_CreateFileIfDoestExist_ShouldReturnEmptyFile() throws IOException {
		var name = "getFile_CreateFileIfDoestExist_ShouldReturnEmptyFile";
		var folderExpected = new File(this.file, name);
		folderExpected.mkdirs();
		var fileExpected = new File(folderExpected, "datafile");
		IOUtil.writeText(name, fileExpected);
		var file = this.dataFileGetter.getFile(name);
		assertTrue(file.length() > 0);
		assertEquals(fileExpected.length(), file.length());
	}

	@Test
	public void getFile_CreateFileIfExists_ShouldReturnNotEmptyFile() throws IOException {
		var file = this.dataFileGetter.getFile("getFile_CreateFileIfDoestExists_ShouldReturnEmptyFile");
		assertEquals(file.length(), 0);
	}

}