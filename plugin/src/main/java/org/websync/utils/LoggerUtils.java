package org.websync.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoggerUtils {

    private final static Calendar calendar = Calendar.getInstance();

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    //SimpleDateFormat 395 usages found // Alex T (5/11/2020)

    public static void print(String message) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        message = String.format("%s - %s", sdf.format(calendar.getTime()), message);
        System.out.println(message);
    }

    private static void writeToLogFile(String message, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.append(message);
        writer.close();
    }

    public static void logeTreeChangeEvent(String message) {
        message = String.format("%s - %s\n", sdf.format(calendar.getTime()), message);
        System.out.println(message);
        try {
            writeToLogFile(message, "c:\\data\\logs\\websync.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
