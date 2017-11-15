package com.depromeet.todo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    private static Date truncate(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    private static Date now() {
        return new Date();
    }

    public static Date add(Date date, long time) {

        return new Date(date.getTime() + time);
    }

    public static Date parse(String date) {

        Date time;

        try {
            time = format.parse(date);
        }
        catch (ParseException e) {
            time = truncate(now());
        }

        return time;
    }
}
