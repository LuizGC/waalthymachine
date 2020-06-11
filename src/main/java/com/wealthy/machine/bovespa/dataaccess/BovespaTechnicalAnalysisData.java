package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.core.sharecode.ShareCode;
import com.wealthy.machine.core.technicalanlysis.AvailableTechnicalAnalysis;
import com.wealthy.machine.core.util.technicalanlysis.EratosthenesSieve;
import com.wealthy.machine.core.util.technicalanlysis.LimitedQueue;
import com.wealthy.machine.core.util.technicalanlysis.type.ValueType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wealthy.machine.core.util.technicalanlysis.type.SupportType.TIME;
import static com.wealthy.machine.core.util.technicalanlysis.type.SupportType.VOLUME;

public class BovespaTechnicalAnalysisData {

	public boolean createAnalysisFile(ShareCode shareCode) {
		try {
			var totalDaysToProcess = 80;
			var primeNumbers = new EratosthenesSieve().findPrimeNumber(totalDaysToProcess);
			var datafile = new File(shareCode.getCode() + File.separator + "datafile");
			var mapper = new ObjectMapper();
			var list = mapper.readValue(datafile, new TypeReference<List<Map<String, String>>>() {
			});
			var queue = new LimitedQueue<Map<String, String>>(primeNumbers.last());
			var matrix = new ArrayList<Object[]>();
			var header = new ArrayList<String>();
			header.add(TIME.toString());
			header.add(VOLUME.toString());
			for (var valueType : ValueType.values()) {
				header.add(valueType.toString());
				for (var primeNumber : primeNumbers) {
					for (var analysis : AvailableTechnicalAnalysis.values()) {
						header.add(analysis.createName(valueType, primeNumber));
					}
				}
			}
			matrix.add(header.toArray());
			for (var cleanMap : list) {
				queue.add(cleanMap);
				if (queue.isCompletelyFilled()) {
					var line = new ArrayList<String>();
					line.add(TIME.getValue(cleanMap));
					line.add(VOLUME.getValue(cleanMap));
					for (var valueType : ValueType.values()) {
						line.add(valueType.getValue(cleanMap));
						for (var primeNumber : primeNumbers) {
							for (var analysis : AvailableTechnicalAnalysis.values()) {
								line.add(analysis.calculate(valueType, queue.sublist(primeNumber)));
							}
						}
					}
					matrix.add(line.toArray());
				}
			}
			var hasAnalysisFile = !matrix.isEmpty();
			if (hasAnalysisFile) {
				mapper.writeValue(new File(datafile.getParentFile(), "analysisdata"), matrix.toArray());
			}
			return hasAnalysisFile;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
