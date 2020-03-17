package org.websync.debugger.testengine;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

public class TestEngine {

    public static String LINE = StringUtils.repeat('-', 80);

    public static void run(Runnable runnable, String testName) {
        System.out.println(LINE);
        System.out.println(String.format("Test '%s' is performing...", testName));
        try {
            runnable.run();
        } catch (Exception ex) {
            System.out.println(String.format("Test failed."));
            System.out.println(ExceptionUtils.getStackTrace(ex));
            System.out.println(LINE);
            return;
        }
        System.out.println(String.format("Test passed."));
        System.out.println(LINE);
    }
}
