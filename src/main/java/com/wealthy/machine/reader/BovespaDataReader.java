package com.wealthy.machine.reader;

import com.wealthy.machine.quote.BovespaStockDailyQuote;
import lombok.Cleanup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

public class BovespaDataReader implements DataReader {

    public Stream<BovespaStockDailyQuote> read(String zipFileUrl) {
        try {
            URL url = new URL(zipFileUrl);
            @Cleanup ZipInputStream zipStream =  new ZipInputStream(url.openStream());
            @Cleanup ReadableByteChannel readableByteChannel = Channels.newChannel(zipStream);
            if (zipStream.getNextEntry() == null) {
                throw new Exception("Zip is not valid!");
            } else {
                Stream<BovespaStockDailyQuote> stream = readEntry(readableByteChannel);

                return stream;
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem to process zip file", e);
        }
    }

    private Stream<BovespaStockDailyQuote> readEntry(ReadableByteChannel readableByteChannel) throws IOException {
        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup WritableByteChannel out = Channels.newChannel(bos);
        ByteBuffer bb = ByteBuffer.allocate(16384);
        while ((readableByteChannel.read(bb)) > 0) {
            bb.flip();
            out.write(bb);
            bb.clear();
        }
        String content = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        return Arrays
                .stream(content.split("\\r?\\n"))
                .map(line -> line.trim())
                .filter(line -> line.length() == 245)
                .map(BovespaStockDailyQuote::new);
    }

}
