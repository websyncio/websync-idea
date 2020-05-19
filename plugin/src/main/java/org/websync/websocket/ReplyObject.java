package org.websync.websocket;


public class ReplyObject {
    private int status;
    String type;
    Object response;

    public ReplyObject() {
    }

    public ReplyObject(Object response) {
        this.response = response;
    }

    public ReplyObject(String type, Object response) {
        this.type = type;
        this.response = response;
    }

    public ReplyObject(int status, Object response) {
        this.status = status;
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
