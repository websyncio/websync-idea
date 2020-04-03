package org.websync.debugger;

import org.websync.logger.Logger;

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
        Logger.print("Command file " + file.getName() + " has changed.");
        if (fileParser == null) {
            Logger.print("File parser is not defined in DebugFileWatcher.");
            return;
        }
        try {
            fileParser.parse(Files.readAllLines(Paths.get(file.getAbsolutePath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
