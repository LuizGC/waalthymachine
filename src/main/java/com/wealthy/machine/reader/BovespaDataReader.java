package com.wealthy.machine.reader;

import com.wealthy.machine.quote.BovespaStockDailyQuote;
import lombok.Cleanup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

public class BovespaDataReader implements DataReader {

    public Set<BovespaStockDailyQuote> read(String zipFileUrl) {
        try {
            URL url = new URL(zipFileUrl);
            @Cleanup ZipInputStream zipStream =  new ZipInputStream(url.openStream());
            if (zipStream.getNextEntry() == null) {
                throw new Exception("Zip is not valid!");
            } else {
                return readEntry(zipStream);
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem to process zip file", e);
        }
    }

    private Set<BovespaStockDailyQuote> readEntry(InputStream zipStream) throws IOException {
        @Cleanup ReadableByteChannel readableByteChannel = Channels.newChannel(zipStream);
        @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @Cleanup WritableByteChannel out = Channels.newChannel(bos);
        ByteBuffer bb = ByteBuffer.allocate(4096);
        while ((readableByteChannel.read(bb)) > 0) {
            bb.flip();
            out.write(bb);
            bb.clear();
        }
        String[] content = new String(bos.toByteArray(), StandardCharsets.UTF_8).split("\\r?\\n");
        return Arrays
                .stream(content)
                .filter(line -> line.trim().length() == 245)
                .map(BovespaStockDailyQuote::new)
                .collect(Collectors.toSet());
    }

}
