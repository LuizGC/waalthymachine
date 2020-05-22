package com.wealthy.machine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class Config {

	private static final String DEFAULT_BOVESPA_URL;
	private static final String DEFAULT_FILENAME;
	private static final String DOWNLOADED_URL_KEY;
	private static final String INITIAL_YEAR;
	private static final String SHARE_CODE_KEY;
	private static final String REPOSITORY_ENV_VAR_NAME ;
	private static final String GIT_SSH_PUBLIC_KEY_ENV_VAR_NAME;
	private static final String GIT_SSH_PRIVATE_KEY_ENV_VAR_NAME;
	private static final String GIT_SSH_PHASSPHRASE_KEY_ENV_VAR_NAME;

	static {
		var appProps = new Properties();
		try (
				var appConfigPath = Thread
						.currentThread()
						.getContextClassLoader()
						.getResourceAsStream("config.properties")
		){
			appProps.load(appConfigPath);
			DEFAULT_BOVESPA_URL = appProps.getProperty("bovespaUrl", "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP");
			INITIAL_YEAR = appProps.getProperty("initialYear", "2000");
			SHARE_CODE_KEY = appProps.getProperty("shareCodeKey", "shareCodes");
			DEFAULT_FILENAME = appProps.getProperty("defaultFilename", "data");
			DOWNLOADED_URL_KEY = appProps.getProperty("downloadedUrlKey", "downloadedUrls");
			REPOSITORY_ENV_VAR_NAME = appProps.getProperty("repositoryEnvVarName", "DATA_REPOSITORY");
			GIT_SSH_PRIVATE_KEY_ENV_VAR_NAME = appProps.getProperty("gitSshPrivateKeyEnvName", "GIT_PRIVATE_SSH_KEY");
			GIT_SSH_PUBLIC_KEY_ENV_VAR_NAME = appProps.getProperty("gitSshPublicKeyEnvName", "GIT_PUBLIC_SSH_KEY");
			GIT_SSH_PHASSPHRASE_KEY_ENV_VAR_NAME = appProps.getProperty("gitSshPassphraseKeyEnvName", "GIT_PASSPHRASE_SSH_KEY");
		} catch (IOException e) {
			var logger = LoggerFactory.getLogger(Config.class);
			logger.error("Error during loading properties", e);
			throw new RuntimeException(e);
		}
	}

	public Config() {
	}

	public Integer getInitialYear() {
		return Integer.valueOf(INITIAL_YEAR);
	}

	public String getDefaultFilename() {
		return DEFAULT_FILENAME;
	}

	public String getDefaultBovespaUrl() {
		return DEFAULT_BOVESPA_URL;
	}

	public Logger getLogger(Class clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	public String getShareCodeKey() {
		return SHARE_CODE_KEY;
	}

	public String getDownloadedUrlKey() {
		return DOWNLOADED_URL_KEY;
	}

	public String getRepositoryPath() {
		return System.getenv(REPOSITORY_ENV_VAR_NAME);
	}

	public String getGitSshPublicKey() {
		return System.getenv(GIT_SSH_PUBLIC_KEY_ENV_VAR_NAME);
	}

	public String getGitSshPrivateKey() {
		return System.getenv(GIT_SSH_PRIVATE_KEY_ENV_VAR_NAME);
	}

	public String getGitSshPassphrase() {
		return System.getenv(GIT_SSH_PHASSPHRASE_KEY_ENV_VAR_NAME);
	}
}