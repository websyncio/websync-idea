package org.websync.connection.messages.idea;

import java.util.List;

public class UrlMatchResult {
    public String websiteId;
    public List<String> pageIds;

    public UrlMatchResult(String websiteId, List<String> pageIds) {
        this.websiteId = websiteId;
        this.pageIds = pageIds;
    }
}
