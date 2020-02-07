package com.wealthy.machine.reader;

import com.wealthy.machine.quote.BovespaStockDailyQuote;
import lombok.Cleanup;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

public class BovespaDataReader implements DataReader {

    private String zipFileUrl;
    byte[] buffer;
    StringBuilder stringBuilder;

    public BovespaDataReader(String zipFileUrl) {
        this.zipFileUrl = zipFileUrl;
        this.buffer = new byte[1];
        this.stringBuilder = new StringBuilder();
    }

    public Stream<BovespaStockDailyQuote> stream() {
        Stream.Builder<BovespaStockDailyQuote> streamBuilder = Stream.builder();
        try {
            URL url = new URL(zipFileUrl);
            @Cleanup InputStream inputStream = url.openStream();
            @Cleanup ZipInputStream zipStream =  new ZipInputStream(inputStream);
            if (zipStream.getNextEntry() == null) {
                throw new Exception("Zip is not valid!");
            } else {
                readEntry(streamBuilder, zipStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return streamBuilder.build();
    }

    private void readEntry(Stream.Builder<BovespaStockDailyQuote> streamBuilder, ZipInputStream zipStream) throws IOException {
        while (zipStream.read(buffer) > 0) {
            String character = new String(buffer, StandardCharsets.UTF_8);
            if (isBreakLine(character)){
                String line = getLine();
                if(line.length() == 245) {
                    streamBuilder.add(new BovespaStockDailyQuote(line));
                }
            } else {
                stringBuilder.append(character);
            }
        }
    }

    private String getLine() {
        String line = stringBuilder.toString().trim();
        stringBuilder.setLength(0);
        return line;
    }

    private boolean isBreakLine(String character) {
        return character.matches("\\r?\\n");
    }
}
