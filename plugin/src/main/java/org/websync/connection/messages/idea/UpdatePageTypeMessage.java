package org.websync.connection.messages.idea;

import org.websync.connection.dto.PageTypeDto;
import org.websync.connection.messages.Message;

public class UpdatePageTypeMessage extends Message {
    public PageTypeDto pageType;

    public UpdatePageTypeMessage(PageTypeDto pageType) {
        super("update-page");
        this.pageType = pageType;
    }
}
