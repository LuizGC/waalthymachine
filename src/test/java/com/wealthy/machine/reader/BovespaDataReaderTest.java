package com.wealthy.machine.reader;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BovespaDataReaderTest {

    @Test
    public void readDataWithSuccess() throws MalformedURLException {
        var zipFilePath = new URL("http://www.b3.com.br/data/files/9C/F3/01/C4/297BE410F816C9E492D828A8/SeriesHistoricas_DemoCotacoesHistoricas12022003.zip");
        var reader = new BovespaDataReader();
        var list = reader.read(zipFilePath);
        assertEquals(282, list.size());
    }

    @Test
    public void invalidUrl() {
        var reader = new BovespaDataReader();
        assertThrows(RuntimeException.class, () -> reader.read(new URL("https://www.uol.com.br/")));
    }

}