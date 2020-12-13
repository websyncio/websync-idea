package org.websync.react.dto;

import org.websync.websession.models.ComponentInstance;

public class ComponentInstanceDto extends BaseDto {
    public String componentType;
    public String name;
    public AnnotationDto initializationAttribute;

    public ComponentInstanceDto() {
    }

    public ComponentInstanceDto(ComponentInstance componentInstance) {
        super(componentInstance.getId());
        componentType = componentInstance.getComponentTypeId();
        name = componentInstance.getName();
        initializationAttribute = new AnnotationDto(componentInstance.getAttributeInstance());
    }
}
