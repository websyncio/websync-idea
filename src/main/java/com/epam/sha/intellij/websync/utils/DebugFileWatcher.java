package com.epam.sha.intellij.websync.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;

public class DebugFileWatcher extends FileWatcher {

    Timer timer;
    FileParser fileParser;

    public DebugFileWatcher(File file, FileParser fileParser) {
        super(file);
        this.fileParser = fileParser;
    }

    public void start() {
        if (timer != null) {
            return;
        }
        timer = new Timer("Timer");
        long delay = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(this, delay, period);
    }

    public void stop() {
        timer.purge();
        timer = null;
    }

    @Override
    protected void onChange(File file) {
        System.out.println("File " + file.getName() + " have change !");
        if (fileParser == null) {
            System.out.println("File parser is not defined in DebugFileWatcher.");
            return;
        }
        try {
            fileParser.parse(Files.readAllLines(Paths.get(file.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
