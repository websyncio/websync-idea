package org.websync.connection.messages;

public class Message {
    public String type;
    public String asyncId;

    public Message() {}

    public Message(String type) {
        this.type=type;
    }
}
