package org.websync.debugger.testengine;

import javafx.util.Pair;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

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
            Runnable runnable = test.getKey();
            String testName = test.getValue();

            if (count < 2) {
                printToOut(LINE);
            }
            printToOut(String.format("TEST [%s] is performing...", testName));
            try {
                runnable.run();
            } catch (Throwable throwable) {
                printToOut(String.format("TEST [%s] FAILED.", testName));
//                throwable.getCause().printStackTrace();
                printToOut(ExceptionUtils.getStackTrace(throwable.getCause()));
                if (count < 2) {
                    printToOut(LINE);
                }
                return;
            }
            printToOut(String.format("TEST [%s] PASSED.", testName));
            if (count < 2) {
                printToOut(LINE);
            }
        });
        count--;
    }

    public static String getTestNameByMethodName(String testMethodName) {
        String testName = StringUtils.capitalize(
                testMethodName.replaceAll("([A-Z])", " $1").toLowerCase()
        );
        return testName;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized public static void printToOut(String line) {
        System.out.println(line);
    }

    synchronized public static void printToErr(String line) {
        System.err.println(line);
    }

    public static void run(Class<?> testClass) {
        count++;
        List<Method> tests = Arrays.stream(testClass.getDeclaredMethods()).filter(m ->
                (Arrays.stream(m.getDeclaredAnnotations()).anyMatch(a -> a.annotationType().getName().contains("Test")))
        ).collect(Collectors.toList());

        tests.stream().forEach(test -> {

            String testMethodName = test.toString();
            String testName = getTestNameByMethodName(test.getName());

            if (count < 2) {
                printToOut(LINE);
            }
            printToOut(String.format("TEST [%s] is performing...", testMethodName));
            try {
                test.invoke(null);
            } catch (Throwable throwable) {
                printToOut(String.format("TEST [%s] FAILED.", testMethodName));
//                throwable.getCause().printStackTrace();
                printToOut(ExceptionUtils.getStackTrace(throwable.getCause()));
                if (count < 2) {
                    printToOut(LINE);
                }
                return;
            }
            printToOut(String.format("TEST [%s] PASSED.", testMethodName));
            if (count < 2) {
                printToOut(LINE);
            }
        });
        count--;
    }

    public static void run(Method method, Object... args) {
        count++;

        Method test = method;
        test.setAccessible(true);

        String testMethodName = test.toString();
        String testName = getTestNameByMethodName(test.getName());

        if (count < 2) {
            printToOut(LINE);
        }
        printToOut(String.format("TEST [%s] is performing...", testMethodName));
        try {
            test.invoke(null, args);
        } catch (Throwable throwable) {
            printToOut(String.format("TEST [%s] FAILED.", testMethodName));
//                throwable.getCause().printStackTrace();
            printToOut(ExceptionUtils.getStackTrace(throwable.getCause()));
            if (count < 2) {
                printToOut(LINE);
            }
            return;
        }
        printToOut(String.format("TEST [%s] PASSED.", testMethodName));
        if (count < 2) {
            printToOut(LINE);
        }
        count--;
    }
}
