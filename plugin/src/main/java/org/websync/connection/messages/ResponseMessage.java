package org.websync.connection.messages;

public class ResponseMessage extends Message {
    public boolean isSuccessful;
    public Object data;
    public String error;

    private static final String responseMessagePostfix = "-response";

    public ResponseMessage(String requestMessageType, boolean isSuccessful) {
        super(requestMessageType + responseMessagePostfix);
        this.isSuccessful = isSuccessful;
    }
}
