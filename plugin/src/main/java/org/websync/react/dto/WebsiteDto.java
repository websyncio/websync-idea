package org.websync.react.dto;

import org.websync.websession.models.Website;

public class WebsiteDto extends PagesContainerDto {
    public String baseWebsiteId;
    public String url;

    public WebsiteDto() {

    }

    public WebsiteDto(Website website) {
        super(website);
        baseWebsiteId = website.getBaseWebsiteId();
        url = website.getUrl();
    }
}