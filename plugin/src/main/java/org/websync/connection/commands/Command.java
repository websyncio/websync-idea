package org.websync.connection.commands;

import org.websync.WebSyncException;

public interface Command {
    Object execute(String commandDataString) throws WebSyncException;
}
