package org.websync.connection.messages.idea;

import org.websync.connection.messages.ProjectMessage;

public class ProjectOpenedMessage extends ProjectMessage {
    public ProjectOpenedMessage(String projectName) {
        super("project-opened", projectName);
    }
}