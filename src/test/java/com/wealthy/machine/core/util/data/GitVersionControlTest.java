package com.wealthy.machine.core.util.data;

import com.wealthy.machine.core.Config;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GitVersionControlTest {

	private File storageFolder;

	@BeforeEach
	public void prepareInstances() throws IOException {
		storageFolder = Files.createTempDirectory("storage_folder").toFile();
	}

	@Test
	public void constructor_NullGivenRepository_ShouldNotCreateGitFolder() throws GitAPIException {
		Config config = mock(Config.class);
		when(config.getLogger(GitVersionControl.class)).thenReturn(LoggerFactory.getLogger(GitVersionControl.class));
		when(config.getRepositoryPath()).thenReturn(null);
		new GitVersionControl(storageFolder, "test", config);
		assertFalse(new File(storageFolder, ".git").exists());
	}

	@Test
	public void constructor_EmptyGivenRepository_ShouldNotCreateGitFolder() throws GitAPIException {
		Config config = mock(Config.class);
		when(config.getLogger(GitVersionControl.class)).thenReturn(LoggerFactory.getLogger(GitVersionControl.class));
		when(config.getRepositoryPath()).thenReturn("");
		new GitVersionControl(storageFolder, "test", config);
		assertFalse(new File(storageFolder, ".git").exists());
	}

	@Test
	public void constructor_IncorrectGivenRepository_ShouldThrowException() {
		Config config = mock(Config.class);
		when(config.getLogger(GitVersionControl.class)).thenReturn(LoggerFactory.getLogger(GitVersionControl.class));
		when(config.getRepositoryPath()).thenReturn("test123");
		when(config.getGitSshPublicKey()).thenReturn("test123");
		when(config.getGitSshPrivateKey()).thenReturn("test123");
		assertThrows(GitAPIException.class, () -> new GitVersionControl(storageFolder, "test", config));
	}

	@Test
	public void constructor_CorrectGivenRepository_ShouldCreateGitFolder() throws GitAPIException {
		Config config = new Config();
		new GitVersionControl(storageFolder, "test", config);
		assertTrue(new File(storageFolder, ".git").exists());
	}

	@Test
	public void push_CorrectGivenRepository_ShouldDownloadTheRandomFile() throws GitAPIException, IOException {
		Config config = new Config();
		var branchName = "test_" + new Random().nextInt();
		var git = new GitVersionControl(storageFolder, branchName, config);
		var file = new File(storageFolder, "deepFolder");
		file.mkdirs();
		file = new File(file, branchName);
		Files.write(file.toPath(), branchName.getBytes());
		git.push();
		storageFolder = Files.createTempDirectory("new_storage_folder").toFile();
		new GitVersionControl(storageFolder, branchName, config);
		assertTrue(new File(storageFolder, branchName).exists());
	}

}