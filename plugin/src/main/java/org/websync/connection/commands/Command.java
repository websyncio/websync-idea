package org.websync.connection.commands;

import org.websync.exceptions.DumbProjectException;
import org.websync.exceptions.WebSyncException;

public interface Command {
    Object execute(String commandDataString) throws WebSyncException, DumbProjectException;
}
