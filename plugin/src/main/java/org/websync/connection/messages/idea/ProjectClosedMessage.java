package org.websync.connection.messages.idea;

import org.websync.connection.messages.ProjectMessage;

public class ProjectClosedMessage extends ProjectMessage {
    public ProjectClosedMessage(String projectName){
        super("project-closed", projectName);
    }
}
