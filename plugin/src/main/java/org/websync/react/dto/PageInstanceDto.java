package org.websync.react.dto;


import org.websync.websession.models.PageInstance;

public class PageInstanceDto extends BaseDto {
    public String pageType;
    public String name;
    public AnnotationDto initializationAttribute;

    public PageInstanceDto() {
    }

    public PageInstanceDto(PageInstance pageInstance) {
        super(pageInstance.getId());
        pageType = pageInstance.getPageTypeId();
        name = pageInstance.getName();
        initializationAttribute = new AnnotationDto(pageInstance.getAttributeInstance());
    }
}