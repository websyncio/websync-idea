package org.websync.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoggerUtils {

    private final static Calendar calendar = Calendar.getInstance();

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    //SimpleDateFormat 395 usages found // Alex T (5/11/2020)

    public static void print(String message) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(String.format("%s - %s", sdf.format(calendar.getTime()), message));
    }
}
