package org.websync.utils;

public class Debouncer {
    private volatile long lastCalled;
    private int interval;

    public Debouncer(int interval) {
        this.interval = interval;
    }

    public void execute(Runnable runnable) {
        if (System.currentTimeMillis() > lastCalled + interval) {
            lastCalled = System.currentTimeMillis();
            runnable.run();
        }
    }
}