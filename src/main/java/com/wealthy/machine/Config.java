package com.wealthy.machine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Properties;

public class Config {

	private static final String BOVESPA_URL;
	private static final String DAILY_SHARE_DATA_BOVESPA_FILENAME;
	private static final String NUMBER_SCALE;
	private static final String INITIAL_YEAR;
	private static final String YEAR_DOWNLOADED_BOVESPA_FILENAME;

	static {
		var appProps = new Properties();
		try (
				var appConfigPath = Thread
						.currentThread()
						.getContextClassLoader()
						.getResourceAsStream("config.properties")
		){
			appProps.load(appConfigPath);
			BOVESPA_URL = appProps.getProperty("bovespaUrl", "http://bvmf.bmfbovespa.com.br/InstDados/SerHist/COTAHIST_A{{YYYY}}.ZIP");
			DAILY_SHARE_DATA_BOVESPA_FILENAME = appProps.getProperty("dailyShareDataBovespaFilename", "dailyShare");
			NUMBER_SCALE = appProps.getProperty("numberScale", "2");
			INITIAL_YEAR = appProps.getProperty("initialYear", "2000");
			YEAR_DOWNLOADED_BOVESPA_FILENAME = appProps.getProperty("yearDownloadedBovespaFilename", "downloadedYear");

		} catch (IOException e) {
			var logger = LoggerFactory.getLogger(Config.class);
			logger.error("Error during loading properties", e);
			throw new RuntimeException(e);
		}
	}

	public Config() {
	}

	public String getDailyShareDataFileName() {
		return DAILY_SHARE_DATA_BOVESPA_FILENAME;
	}

	public Integer getNumberScale() {
		return Integer.valueOf(NUMBER_SCALE);
	}

	public Integer getInitialYear() {
		return Integer.valueOf(INITIAL_YEAR);
	}

	public String getYearDownloadedBovespaFilename() {
		return YEAR_DOWNLOADED_BOVESPA_FILENAME;
	}

	public String getDailyShareDataBovespaFilename() {
		return DAILY_SHARE_DATA_BOVESPA_FILENAME;
	}

	public String getDefaultBovespaUrl() {
		return BOVESPA_URL;
	}

	public ObjectMapper getJsonMapper() {
		return new ObjectMapper();
	}

	public MathContext getMathContext() {
		return MathContext.DECIMAL128;
	}

	public RoundingMode getRoundingMode() {
		return RoundingMode.HALF_EVEN;
	}

	public Logger getLogger(Class clazz) {
		return LoggerFactory.getLogger(clazz);
	}
}