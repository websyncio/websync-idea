package org.websync.connection.messages.idea;

import org.websync.connection.messages.Message;

public class ShowPageObjectMessage extends Message {
    public String pageObjectId;

    public ShowPageObjectMessage(String pageObjectType, String pageObjectId) {
        super("show-" + pageObjectType);
        this.pageObjectId = pageObjectId;
    }
}
