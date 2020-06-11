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

public class BovespaTechnicalAnalysisData {

	public boolean createAnalysisFile(ShareCode shareCode) {
		try {
			var totalDaysToProcess = 80;
			var primeNumbers = new EratosthenesSieve().findPrimeNumber(totalDaysToProcess);
			var datafile = new File(shareCode.getCode() + File.separator + "datafile");
			var mapper = new ObjectMapper();
			var list = mapper.readValue(datafile, new TypeReference<List<Map<String, Double>>>() {
			});
			var queue = new LimitedQueue<Map<String, Double>>(primeNumbers.last());
			var matrix = new ArrayList<Double[]>();
			for (var cleanMap : list) {
				queue.add(cleanMap);
				if (queue.isCompletelyFilled()) {
					var line = new ArrayList<Double>();
					for (var valueType : ValueType.values()) {
						line.add(valueType.getValue(cleanMap));
						for (var primeNumber : primeNumbers) {
							for (var analysis : AvailableTechnicalAnalysis.values()) {
								line.add(analysis.calculate(valueType, queue.sublist(primeNumber)));
							}
						}
					}
					matrix.add(line.toArray(new Double[0]));
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
