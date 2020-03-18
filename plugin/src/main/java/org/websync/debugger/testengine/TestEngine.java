package org.websync.debugger.testengine;

import javafx.util.Pair;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.websync.debugger.commands.CommandTestRun;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static void run(Class<?> testClass) {
        List<Method> tests = Arrays.stream(testClass.getDeclaredMethods()).filter(m -> {
            return (Arrays.stream(m.getDeclaredAnnotations()).anyMatch(a -> a.annotationType().getName().contains("Test")));
        }).collect(Collectors.toList());

        tests.stream().forEach(test -> {

            if (count < 2) {
                System.out.println(LINE);
            }
            System.out.println(String.format("Test [%s] is performing...", test.getName()));
            try {
                test.invoke(null);
            } catch (Throwable throwable) {

                System.out.println(String.format("Test [%s] failed.", test.getName()));
                System.out.println(ExceptionUtils.getStackTrace(throwable.getCause()));
//                throwable.getCause().printStackTrace();
                System.out.println(LINE);
                return;
            }
            System.out.println(String.format("Test [%s] passed.", test.getName()));
            if (count < 2) {
                System.out.println(LINE);
            }
        });
    }
}
