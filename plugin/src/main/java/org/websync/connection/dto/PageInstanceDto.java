package org.websync.connection.dto;


import org.websync.models.PageInstance;
import org.websync.psi.models.AnnotationInstance;

import java.util.List;

public class PageInstanceDto extends BaseDto {
    public String pageType;
    public String name;
    public String url;

    public PageInstanceDto() {
    }

    public PageInstanceDto(PageInstance pageInstance) {
        super(pageInstance.getId());
        pageType = pageInstance.getPageTypeId();
        name = pageInstance.getName();
        url = pageInstance.getUrl();
    }
}