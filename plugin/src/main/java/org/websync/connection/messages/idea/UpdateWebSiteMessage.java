package org.websync.connection.messages.idea;

import org.websync.connection.dto.WebsiteDto;
import org.websync.connection.messages.Message;

public class UpdateWebSiteMessage extends Message {
    public WebsiteDto website;

    public UpdateWebSiteMessage(WebsiteDto website) {
        super("update-website");
        this.website = website;
    }
}
