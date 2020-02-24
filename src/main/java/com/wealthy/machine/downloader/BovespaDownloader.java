package com.wealthy.machine.downloader;

import com.wealthy.machine.StockExchange;
import lombok.Cleanup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class BovespaDownloader implements Downloader {

    private final File folder;
    private final File downloadedHistoric;

    public BovespaDownloader(String destinyPath) {
        this.folder = getDestinyFolder(destinyPath);
        this.downloadedHistoric = new File(folder, "downloadedHistoric");
        createFilesIfNecessary();
    }

    private void createFilesIfNecessary() {
        try {
            if(!this.downloadedHistoric.exists()) {
                this.downloadedHistoric.createNewFile();
            }
        } catch (Exception e) {
            throw new RuntimeException("Problems to create the database.", e);
        }
    }

    private File getDestinyFolder(String destinyPath) {
        File folder = new File(destinyPath);
        if (!folder.exists()) {
            throw new RuntimeException("Destiny folder must exist!");
        }
        if (folder.isFile()) {
            throw new RuntimeException("Destiny folder must be a folder!");
        }
        if (!folder.canWrite()) {
            throw new RuntimeException("Destiny folder must be writeable!");
        }
        if (!folder.canRead()) {
            throw new RuntimeException("Destiny folder must be readable!");
        }
        folder = new File(folder, StockExchange.BOVESPA.name());
        folder.mkdir();
        return folder;
    }

    public boolean isDownloaded(String url) {
        try{
            return Files
                    .readAllLines(this.downloadedHistoric.toPath())
                    .stream()
                    .anyMatch(downloadedUrl -> downloadedUrl.equals(url));
        }catch (Exception e) {
            throw new RuntimeException("Problems in testing if the url is downloaded", e);
        }
    }

    public void addDownloadedUrl(String url) {
        try {
            @Cleanup PrintWriter printer = new PrintWriter(this.downloadedHistoric);
            printer.println(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
