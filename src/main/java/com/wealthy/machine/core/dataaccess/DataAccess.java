package com.wealthy.machine.core.dataaccess;

import java.io.IOException;
import java.util.Set;

public interface DataAccess<T> {

	void save(Set<T> dailyQuoteSet) throws IOException;

}
