package org.websync.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoggerUtils {

    final static Calendar calendar = Calendar.getInstance();
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static void print(String message) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(String.format("%s - %s", sdf.format(calendar.getTime()), message));
    }
}
