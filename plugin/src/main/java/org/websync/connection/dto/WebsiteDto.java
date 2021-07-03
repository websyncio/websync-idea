package org.websync.connection.dto;

import org.websync.models.WebSite;

import java.util.List;
import java.util.stream.Collectors;

public class WebsiteDto extends BaseDto {
    public List<PageInstanceDto> pageInstances;
    public String baseWebSite;
    public String url;

    public WebsiteDto() {
    }

    public WebsiteDto(WebSite website) {
        super(website.getId());
        baseWebSite = website.getBaseWebsiteId();
        url = website.getUrl();

        pageInstances = website.getPageInstances()
                .stream().map(PageInstanceDto::new)
                .collect(Collectors.toList());
    }
}