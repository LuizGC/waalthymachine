package com.wealthy.machine.reader;

import com.wealthy.machine.share.BovespaDailyShare;
import com.wealthy.machine.share.DailyShare;
import com.wealthy.machine.sharecode.BovespaShareCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

public class BovespaDataReader implements DataReader {

    public Set<DailyShare> read(URL zipFileUrl) {
        try (ZipInputStream zipStream = new ZipInputStream(zipFileUrl.openStream())) {
            if (zipStream.getNextEntry() == null) {
                throw new IOException("Zip is not valid!");
            } else {
                return readEntry(zipStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem to process zip file", e);
        }
    }

    private Set<DailyShare> readEntry(InputStream zipStream) throws IOException {
        try (
                var readableByteChannel = Channels.newChannel(zipStream);
                var bos = new ByteArrayOutputStream();
                var out = Channels.newChannel(bos)
        ){
            var bb = ByteBuffer.allocate(4096);
            while ((readableByteChannel.read(bb)) > 0) {
                bb.flip();
                out.write(bb);
                bb.clear();
            }
            var content = new String(bos.toByteArray(), StandardCharsets.UTF_8).split("\\r?\\n");
            return Arrays
                    .stream(content)
                    .filter(line -> line.trim().length() == 245)
                    .map(this::createQuote)
                    .filter(BovespaDailyShare::isAvaliableInCashMarket)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    private BovespaDailyShare createQuote(String line) {
        try {
            line = line.trim();
            var dateFormat = new SimpleDateFormat("yyyyMMdd");
            var tradingDay = dateFormat.parse(line.substring(2, 10).trim());
            var stockCode = new BovespaShareCode(readString(line, 12, 24));
            var company = readString(line, 27, 39);
            var openPrice = readDouble(line, 56, 69);
            var closePrice = readDouble(line, 108, 121);
            var minPrice = readDouble(line, 82, 95);
            var maxPrice = readDouble(line, 69, 82);
            var avgPrice = readDouble(line, 97, 108);
            var volume = readDouble(line, 170, 188);
            return new BovespaDailyShare(
                    tradingDay,
                    stockCode,
                    company,
                    openPrice,
                    closePrice,
                    minPrice,
                    maxPrice,
                    avgPrice,
                    volume
            );
        } catch (ParseException e) {
            throw new RuntimeException("The Bovespa quote date is invalid.", e);
        }
    }

    private String readString(String line, Integer begin, Integer end) {
        return line.substring(begin, end).trim();
    }

    private Double readDouble(String line, Integer begin, Integer end) {
        return  Double.parseDouble(readString(line, begin, end))/100;
    }

}
