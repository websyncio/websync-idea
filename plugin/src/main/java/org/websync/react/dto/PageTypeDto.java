package org.websync.react.dto;

import org.websync.websession.models.PageType;

public class PageTypeDto extends ComponentsContainerDto {
    public String basePageTypeId;
    public String url;

    public PageTypeDto() {

    }

    public PageTypeDto(PageType pageType) {
        super(pageType);
        basePageTypeId = pageType.getBasePageTypeId();
        url = pageType.getUrl();
    }
}
