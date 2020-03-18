package org.websync.debugger.testengine;

import javafx.util.Pair;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class TestEngine {

    public static String LINE = StringUtils.repeat('-', 80);

    public static void run(Pair<Runnable, String> ... tests) {
        Arrays.asList(tests).stream().forEach(test -> {
            System.out.println(LINE);
            System.out.println(String.format("Test '%s' is performing...", test.getValue()));
            try {
                test.getKey().run();
            } catch (Throwable ex) {
                System.out.println(String.format("Test failed."));
                System.out.println(ExceptionUtils.getStackTrace(ex));
                System.out.println(LINE);
                return;
            }
            System.out.println(String.format("Test passed."));
            System.out.println(LINE);
        });
    }
}
