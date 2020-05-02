package com.wealthy.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Config {

	private static final String DEFAULT_BOVESPA_URL;
	private static final String NUMBER_SCALE;
	private static final String INITIAL_YEAR;
	private static final String DEFAULT_FILENAME;
	private static final Executor DEFAULT_EXECUTOR;

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
			NUMBER_SCALE = appProps.getProperty("numberScale", "2");
			INITIAL_YEAR = appProps.getProperty("initialYear", "2000");
			DEFAULT_FILENAME = appProps.getProperty("defaultFilename", "data");
			DEFAULT_EXECUTOR = Executors.newCachedThreadPool();
		} catch (IOException e) {
			var logger = LoggerFactory.getLogger(Config.class);
			logger.error("Error during loading properties", e);
			throw new RuntimeException(e);
		}
	}

	public Config() {
	}

	public Integer getNumberScale() {
		return Integer.valueOf(NUMBER_SCALE);
	}

	public Integer getInitialYear() {
		return Integer.valueOf(INITIAL_YEAR);
	}

	public String getDefaultFilename() {
		return DEFAULT_FILENAME;
	}


	public Executor getDefaultExecutor() {
		return DEFAULT_EXECUTOR;
	}

	public String getDefaultBovespaUrl() {
		return DEFAULT_BOVESPA_URL;
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