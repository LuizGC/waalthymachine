package com.wealthy.machine.reader;

import com.wealthy.machine.quote.BovespaDailyQuote;
import com.wealthy.machine.quote.DailyQuote;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCode;
import com.wealthy.machine.sharecode.bovespa.BovespaShareCodeValidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

public class BovespaDataReader implements DataReader {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public Set<DailyQuote> read(URL zipFileUrl) {
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

    private Set<DailyQuote> readEntry(InputStream zipStream) throws IOException {
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
                    .filter(this::isValidLine)
                    .map(this::createQuote)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    private Boolean isValidLine(String line) {
        line = line.trim();
        if(line.length() != 245){
            return false;
        }
        var shareCode = getShareCode(line);
        var validador = new BovespaShareCodeValidator(shareCode);
        return validador.isCorrectSize() &&
                validador.isFourInitialsOnlyLetter() &&
                validador.isShareCashMarketAllowed();
    }

    private String getShareCode(String line) {
        return readString(line, 12, 24);
    }

    private BovespaDailyQuote createQuote(String line) {
        try {
            line = line.trim();
            var tradindDate = DATE_FORMAT.parse(readString(line, 2, 10));
            return new BovespaDailyQuote(
                    tradindDate, //tradingDay
                    new BovespaShareCode(getShareCode(line)), //stockCode
                    readString(line, 27, 39), //company
                    readDouble(line, 56, 69), //openPrice
                    readDouble(line, 108, 121), //closePrice
                    readDouble(line, 82, 95), //minPrice
                    readDouble(line, 69, 82), //maxPrice
                    readDouble(line, 97, 108), //avgPrice
                    readInteger(line, 170, 188) //volume
            );
        } catch (ParseException e) {
            throw new RuntimeException("The Bovespa quote date is invalid.", e);
        }
    }

    private Integer readInteger(String line, int begin, int end) {
        return Integer.parseInt(readString(line, begin, end));
    }

    private String readString(String line, Integer begin, Integer end) {
        return line.substring(begin, end).trim();
    }

    private Double readDouble(String line, Integer begin, Integer end) {
        return  Double.parseDouble(readString(line, begin, end))/100;
    }

}
