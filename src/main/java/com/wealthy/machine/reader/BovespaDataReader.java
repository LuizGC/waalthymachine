package com.wealthy.machine.reader;

import com.wealthy.machine.quote.BovespaStockDailyQuote;
import lombok.Cleanup;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

public class BovespaDataReader implements DataReader {

    public Stream<BovespaStockDailyQuote> read(String zipFileUrl) {
        try {
            URL url = new URL(zipFileUrl);
            Path tempFile = Files.createTempFile("temp_quotes", ".zip");
            @Cleanup ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile());
            @Cleanup FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            @Cleanup ZipInputStream zipStream =  new ZipInputStream(new FileInputStream(tempFile.toFile()));
            if (zipStream.getNextEntry() == null) {
                throw new Exception("Zip is not valid!");
            } else {
                Stream<BovespaStockDailyQuote> stream = readEntry(zipStream);
                tempFile.toFile().delete();
                return stream;
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem to process zip file", e);
        }
    }

    private Stream<BovespaStockDailyQuote> readEntry(ZipInputStream zipStream) throws IOException {
        byte [] buffer = new byte[16384];
        int totalRead;
        String stringBuffer = "";
        while ((totalRead = zipStream.read(buffer)) > 0) {
            stringBuffer += new String(buffer, StandardCharsets.UTF_8).substring(0, totalRead);
        }
        return Arrays
                .stream(stringBuffer.split("\\r?\\n"))
                .map(line -> line.trim())
                .filter(line -> line.length() == 245)
                .map(BovespaStockDailyQuote::new);
    }

}
