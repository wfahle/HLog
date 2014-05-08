package com.wfahle.hlog.utils;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateTimeUtils {
    private static final DateFormat DATE_TIME_OUTPUT_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);    

   public static CharSequence getDateTime(long millis) {
           DATE_TIME_OUTPUT_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
           return DATE_TIME_OUTPUT_FORMAT.format(new Date(millis));
      }
}
