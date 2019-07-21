package com.burakkal.taskplanner.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String toString(long ms) {
        String format = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(ms);
        return sdf.format(date);
    }

    public static long getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public static long getDayAfter(long date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime().getTime();
    }
}
