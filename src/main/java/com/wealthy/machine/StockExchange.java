package com.wealthy.machine;

import java.io.File;
import java.io.Serializable;

public enum StockExchange implements Serializable {
    BOVESPA;

    public File getFolder(File mainFolder) {
        var stockFolder = new File(mainFolder, this.name());
        stockFolder.mkdirs();
        return stockFolder;
    }
}