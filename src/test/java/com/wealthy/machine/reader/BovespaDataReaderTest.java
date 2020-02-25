package com.wealthy.machine.reader;

import com.wealthy.machine.quote.BovespaStockDailyQuote;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BovespaDataReaderTest {

    @Test
    public void readDataWithSuccess() {
        String zipFilePath = "http://www.b3.com.br/data/files/9C/F3/01/C4/297BE410F816C9E492D828A8/SeriesHistoricas_DemoCotacoesHistoricas12022003.zip";
        BovespaDataReader reader = new BovespaDataReader();
        Stream<BovespaStockDailyQuote> stream = reader.read(zipFilePath);
        assertEquals(551, stream.count());
    }

    @Test
    public void invalidUrl() {
        BovespaDataReader reader = new BovespaDataReader();
        assertThrows(RuntimeException.class, () -> reader.read("https://www.uol.com.br/"));
    }
}