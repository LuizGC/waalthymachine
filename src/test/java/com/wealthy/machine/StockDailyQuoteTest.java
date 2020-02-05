package com.wealthy.machine;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class StockDailyQuoteTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private String getCorrectLine(){
        return "012003021202VALE3       010VALE R DOCE ON           R$  000000001050100000000105010000000010250000000001036800000000103210000000010321000000001043800142000000000000069500000000000720641400000000000000009999123100000010000000000000BRVALEACNOR0159";
    }

    @Test
    public void createDataInfo() {
        StockDailyQuote stockDailyQuote = new StockDailyQuote(getCorrectLine());
        assertEquals("StockDailyQuote{tradingDay=Wed Feb 12 00:00:00 CET 2003, stockCode='VALE3', company='VALE R DOCE', openPrice=105.01, closePrice=103.21, minPrice=102.5, maxPrice=105.01, avgPrice=103.68}", stockDailyQuote.toString());
    }

    @Test
    public void errorCreateDataInfoWhenDateIsInvalid() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Date is invalid!");
        String line = getCorrectLine();
        line = line.replaceFirst("2003", "200A");
        new StockDailyQuote(line);
    }

    @Test
    public void errorCreateLineWhenLineIsDifferentThan245() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Line size must be 245!");
        String line = getCorrectLine();
        line = line.substring(0, line.length() - 3);
        new StockDailyQuote(line);
    }


}
