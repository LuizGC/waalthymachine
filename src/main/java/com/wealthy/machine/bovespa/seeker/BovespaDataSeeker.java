package com.wealthy.machine.bovespa.seeker;

import com.wealthy.machine.Config;
import com.wealthy.machine.core.math.number.WealthNumber;
import com.wealthy.machine.bovespa.quote.BovespaDailyQuote;
import com.wealthy.machine.core.quote.DailyQuote;
import com.wealthy.machine.core.seeker.DataSeeker;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCode;
import com.wealthy.machine.bovespa.sharecode.BovespaShareCodeValidator;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

public class BovespaDataSeeker implements DataSeeker {

    private final Logger logger;

    public BovespaDataSeeker() {
        var config = new Config();
        this.logger = config.getLogger(this.getClass());
    }

    public Set<DailyQuote> read(URL zipFileUrl) {
        this.logger.info("Starting download url={}", zipFileUrl);
        try (ZipInputStream zipStream = new ZipInputStream(zipFileUrl.openStream())) {
            if (zipStream.getNextEntry() == null) {
                throw new IOException("Zip is not valid!");
            } else {
                var quotes = readEntry(zipStream);
                this.logger.info("Download completed url={}", zipFileUrl);
                return quotes;
            }
        } catch (IOException e) {
            this.logger.error("Problem to process zip file", e);
            throw new RuntimeException(e);
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
            return new BovespaDailyQuote(
                    readDate(line, 2, 10), //tradingDay
                    new BovespaShareCode(getShareCode(line)), //stockCode
                    readDouble(line, 56, 69), //openPrice
                    readDouble(line, 108, 121), //closePrice
                    readDouble(line, 82, 95), //minPrice
                    readDouble(line, 69, 82), //maxPrice
                    readDouble(line, 97, 108), //avgPrice
                    readDouble(line, 170, 188) //volume
            );
        } catch (ParseException e) {
            this.logger.error("The Bovespa quote date is invalid.", e);
            throw new RuntimeException(e);
        }
    }

    private Date readDate(String line, Integer begin, Integer end) throws ParseException {
        return new SimpleDateFormat("yyyyMMdd").parse(readString(line, begin, end));
    }

    private String readString(String line, Integer begin, Integer end) {
        return line.substring(begin, end).trim();
    }

    private WealthNumber readDouble(String line, Integer begin, Integer end) {
        var text = readString(line, begin, end);
        var number = new BigDecimal(text).movePointLeft(2);
        return new WealthNumber(number.toPlainString());
    }

}
