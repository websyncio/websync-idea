package org.websync.connection.messages.browser;

import org.websync.connection.dto.ComponentInstanceDto;
import org.websync.connection.messages.ProjectMessage;

public class ComponentInstanceMessage extends ProjectMessage {
    public ComponentInstanceDto componentInstance;

    public ComponentInstanceMessage(String type, String projectName) {
        super(type, projectName);
    }
}
