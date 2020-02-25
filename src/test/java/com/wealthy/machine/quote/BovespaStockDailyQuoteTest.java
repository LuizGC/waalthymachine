package com.wealthy.machine.quote;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BovespaStockDailyQuoteTest {

    private String getLine(){
        return "012003021202VALE3       010VALE R DOCE ON           R$  000000001050100000000105010000000010250000000001036800000000103210000000010321000000001043800142000000000000069500000000000720641400000000000000009999123100000010000000000000BRVALEACNOR0159";
    }

    private String getObjectTested(){
        return "BovespaStockDailyQuote(tradingDay=Wed Feb 12 00:00:00 CET 2003, stockCode=VALE3, company=VALE R DOCE, openPrice=105.01, closePrice=103.21, minPrice=102.5, maxPrice=105.01, avgPrice=103.68, volume=7206414.0)";
    }

    @Test
    public void createDataInfo() {
        StockDailyQuote bovespaStockDailyQuote = new BovespaStockDailyQuote(getLine());
        assertEquals(getObjectTested(), bovespaStockDailyQuote.toString());
    }

    @Test
    public void errorCreateDataInfoWhenDateIsInvalid() {
        assertThrows(RuntimeException.class, () -> {
            String line = getLine();
            line = line.replaceFirst("2003", "200A");
            new BovespaStockDailyQuote(line);
        }, "Date is invalid!");
    }

    @Test
    public void errorCreateLineWhenLineIsDifferentThan245() {
        assertThrows(RuntimeException.class, () -> {
            String line = getLine();
            line = line.substring(0, line.length() - 3);
            new BovespaStockDailyQuote(line);
        }, "Line size must be 245!");
    }
}
