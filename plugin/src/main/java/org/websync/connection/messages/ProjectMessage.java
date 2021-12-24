package org.websync.connection.messages;

public class ProjectMessage extends Message {
    public String projectName;

    public ProjectMessage(){}

    public ProjectMessage(String type, String projectName) {
        super(type);
        this.projectName = projectName;
    }
}
