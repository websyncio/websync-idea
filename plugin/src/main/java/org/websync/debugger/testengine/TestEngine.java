package org.websync.debugger.testengine;

import javafx.util.Pair;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class TestEngine {

    public static String LINE = StringUtils.repeat('-', 80);
    public static int count = 0;

    public static void run(Pair<Runnable, String>... tests) {
        count++;
        Arrays.asList(tests).stream().forEach(test -> {
            Runnable testMethod = test.getKey();
            String testName = test.getValue();

            if (count < 2) {
                System.out.println(LINE);
            }
            System.out.println(String.format("Test [%s] is performing...", testName));
            try {
                testMethod.run();
            } catch (Throwable ex) {
                System.out.println(String.format("Test failed."));
                System.out.println(ExceptionUtils.getStackTrace(ex));
                System.out.println(LINE);
                return;
            }
            System.out.println(String.format("Test [%s] passed.", testName));
            if (count < 2) {
                System.out.println(LINE);
            }
        });
        count--;
    }
}
