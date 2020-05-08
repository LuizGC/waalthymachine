package com.wealthy.machine.bovespa.dataaccess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealthy.machine.Config;
import com.wealthy.machine.core.sharecode.ShareCode;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BovespaShareCodeDataAccess {
	private final File fileShareCodes;
	private final Logger logger;
	private final String filename;

	public BovespaShareCodeDataAccess(File bovespaFolder) {
		var config = new Config();
		this.logger = config.getLogger(this.getClass());
		this.filename = config.getDefaultFilename();
		var folderShareCodes = new File(bovespaFolder, "shareCodes");
		folderShareCodes.mkdirs();
		this.fileShareCodes = new File(folderShareCodes, filename);
		try {
			this.fileShareCodes.createNewFile();
		} catch (IOException e) {
			logger.error("Erro while creatinf file shared file", e);
			throw new RuntimeException(e);
		}
	}

	public synchronized void updateDownloadedShareCodes(Set<ShareCode> shareCodes) throws IOException {
		var setSave = new HashSet<>(shareCodes);
		setSave.addAll(listShareCodesSaved());
		var mapper = new ObjectMapper();
		mapper.writeValue(this.fileShareCodes, setSave);
	}

	private Set<BovespaShareCode> listShareCodesSaved() {
		try{
			var typeReference = new TypeReference<HashSet<BovespaShareCode>>() {};
			var mapper = new ObjectMapper();
			var shareCodes = mapper.readValue(this.fileShareCodes, typeReference);
			return Collections.unmodifiableSet(shareCodes);
		} catch (Exception e) {
			return Collections.emptySet();
		}
	}
}
