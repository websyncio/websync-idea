package org.websync.connection.dto;

import org.websync.models.PageType;

public class PageTypeDto extends ComponentsContainerDto {
    public String url;

    public PageTypeDto() {
    }

    public PageTypeDto(PageType pageType) {
        super(pageType);
        url = pageType.getUrl();
    }
}
