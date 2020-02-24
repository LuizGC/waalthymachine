package com.wealthy.machine.downloader;

import com.wealthy.machine.StockExchange;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BovespaDownloaderTest {

    @Test
    public void testDownloadedControl() throws IOException {
        Path destinyFolderPath = Files.createTempDirectory("destinyFolder");
        File bovespaFile = new File(destinyFolderPath.toFile(), StockExchange.BOVESPA.name());
        BovespaDownloader database = new BovespaDownloader(destinyFolderPath.toString());
        assertTrue("Bovespa path exists", bovespaFile.exists());
        assertTrue("Bovespa path is a folder", bovespaFile.isDirectory());
        String urlTested1 = "urlTested1";
        database.addDownloadedUrl(urlTested1);
        assertTrue("urlTested1 exists", database.isDownloaded(urlTested1));
        String urlTested2 = "urlTested2";
        database.addDownloadedUrl(urlTested2);
        assertTrue("urlTested2 exists", database.isDownloaded(urlTested2));
        String urlTested3 = "urlTested3";
        assertFalse("urlTested3 doesn't exist", database.isDownloaded(urlTested3));

    }

}