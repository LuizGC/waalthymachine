package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.core.Config;
import com.wealthy.machine.core.dataaccess.DataAccess;
import com.wealthy.machine.core.seeker.MissingUrlSeeker;
import com.wealthy.machine.core.util.DataFileGetter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BovespaUrlDataAccess implements DataAccess<URL>, MissingUrlSeeker {

	private final Integer initialYear;
	private final String defaultUrl;
	private final Logger logger;
	private final File yearFile;

	public BovespaUrlDataAccess(DataFileGetter dataFileGetter, Config config) {
		this.initialYear = config.getInitialYear();
		this.defaultUrl = config.getDefaultBovespaUrl();
		this.logger = config.getLogger(this.getClass());
		this.yearFile =dataFileGetter.getFile(config.getDownloadedUrlKey());
	}

	@Override
	public synchronized void save(Set<URL> newYearsSet) {
		var yearSet = new HashSet<>(listSavedYears());
		yearSet.addAll(newYearsSet);
		try{
			var mapper = new ObjectMapper();
			mapper.writeValue(this.yearFile, yearSet);
		} catch (Exception e) {
			logger.error("There is an issue during saving the daily share set", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<URL> listMissingUrl() {
		var savedYearSet = listSavedYears();
		return IntStream
				.range(initialYear, Year.now().getValue() + 1)
				.mapToObj(this::createUrl)
				.filter(url -> !savedYearSet.contains(url))
				.collect(Collectors.toUnmodifiableSet());
	}

	private Set<URL> listSavedYears() {
		try {
			var typeReference = new TypeReference<LinkedHashSet<URL>>() {};
			var mapper = new ObjectMapper();
			var quotesSet = mapper.readValue(this.yearFile, typeReference);
			return Collections.unmodifiableSet(quotesSet);
		} catch (IOException e) {
			return Collections.emptySet();
		}
	}

	private URL createUrl(Integer year) {
		try {
			String urlPath = defaultUrl.replace("{{YYYY}}", String.valueOf(year));
			return new URL(urlPath);
		} catch (MalformedURLException e) {
			logger.error("Error during Bovespa Url creation.", e);
			throw new RuntimeException(e);
		}
	}

	public Set<URL> convertDateToBovespaUrl(Set<Date> quoteDates) {
		return quoteDates
				.stream()
				.map(this::getYear)
				.map(this::createUrl)
				.collect(Collectors.toUnmodifiableSet());
	}

	private Integer getYear(Date date) {
		var calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
}
