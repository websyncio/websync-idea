package org.websync.connection.messages.browser;

import org.websync.connection.messages.ProjectMessage;

public class CreateComponentTypeMessage extends ProjectMessage {
    public String name;
    public String parentType;
    public String baseType;
}
