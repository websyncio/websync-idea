package org.websync.connection.dto;


import org.websync.models.PageInstance;

import java.util.List;

public class PageInstanceDto extends BaseDto {
    public String pageType;
    public String name;
    public String url;
    public AnnotationDto initializationAttribute;

    public PageInstanceDto() {
    }

    public PageInstanceDto(PageInstance pageInstance) {
        super(pageInstance.getId());
        pageType = pageInstance.getPageTypeId();
        name = pageInstance.getName();
        url = "";

        initializationAttribute = new AnnotationDto(pageInstance.getAttributeInstance());

//        AnnotationDto initializationAttribute = new AnnotationDto(pageInstance.getAttributeInstance());
//        AnnotationDto initializationAttribute = new AnnotationDto(pageInstance.getAttributeInstance());
        if(initializationAttribute.getName().equals("Url")){
            List<AnnotationDto.Parameter> params = initializationAttribute.getParameters();
            url = initializationAttribute.getParameters().get(0).getValues().get(0);
        }
    }
}