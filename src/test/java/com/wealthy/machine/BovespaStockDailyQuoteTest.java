package com.wealthy.machine;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class BovespaStockDailyQuoteTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private String getLine(){
        return "012003021202VALE3       010VALE R DOCE ON           R$  000000001050100000000105010000000010250000000001036800000000103210000000010321000000001043800142000000000000069500000000000720641400000000000000009999123100000010000000000000BRVALEACNOR0159";
    }

    @Test
    public void createDataInfo() {
        BovespaStockDailyQuote bovespaStockDailyQuote = new BovespaStockDailyQuote(getLine());
        assertEquals("BovespaStockDailyQuote(tradingDay=Wed Feb 12 00:00:00 CET 2003, stockCode=VALE3, company=VALE R DOCE, openPrice=105.01, closePrice=103.21, minPrice=102.5, maxPrice=105.01, avgPrice=103.68, volume=7206414.0)", bovespaStockDailyQuote.toString());
    }

    @Test
    public void errorCreateDataInfoWhenDateIsInvalid() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Date is invalid!");
        String line = getLine();
        line = line.replaceFirst("2003", "200A");
        new BovespaStockDailyQuote(line);
    }

    @Test
    public void errorCreateLineWhenLineIsDifferentThan245() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Line size must be 245!");
        String line = getLine();
        line = line.substring(0, line.length() - 3);
        new BovespaStockDailyQuote(line);
    }


}
