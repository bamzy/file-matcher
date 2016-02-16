package com.bamzy.insurance.matcher;


import com.samanpr.jalalicalendar.JalaliCalendar;
import org.junit.Assert;

import java.util.Calendar;

public class BatchFilePaymentTest {

    @org.junit.Test
    public void testGetFormattedTime() throws Exception {
        BatchFilePayment batchFilePayment = new BatchFilePayment();


        for (int i = 0; i < 366; i++) {
            Calendar calendar = new JalaliCalendar();
            calendar.add(Calendar.DATE, -i);
            String time = batchFilePayment.getFormattedTime(calendar);
            Assert.assertEquals(10, time.length());
        }

    }
}