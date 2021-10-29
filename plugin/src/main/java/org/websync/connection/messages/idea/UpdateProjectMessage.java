package org.websync.connection.messages.idea;

import org.websync.connection.dto.JdiProjectDto;
import org.websync.connection.messages.Message;

public class UpdateProjectMessage extends Message {
    public JdiProjectDto project;

    public UpdateProjectMessage(JdiProjectDto project) {
        super("update-project");
        this.project = project;
    }
}