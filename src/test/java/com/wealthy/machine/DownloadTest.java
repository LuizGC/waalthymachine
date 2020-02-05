package com.wealthy.machine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DownloadTest {
    @Test
    public void addition() {
        Download download = new Download();
        assertEquals(true, download.downloaded());
    }

}
