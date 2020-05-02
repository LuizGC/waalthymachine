package com.wealthy.machine.dataaccesslayer.persistlayer;

import java.io.IOException;

public interface PersistLayer<T> {

	T readFileContent(String key) throws IOException;
	void saveData(String key, Object data) throws IOException;

}
