package org.websync.react.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.websync.websession.models.ComponentInstance;

public class ComponentInstanceDto extends BaseDto {
    public String componentTypeId;
    public String name;
    public AnnotationDto initializationAttribute;

    public ComponentInstanceDto(ComponentInstance componentInstance) {
        super(componentInstance.getId());
        componentTypeId = componentInstance.getComponentTypeId();
        name = componentInstance.getName();
        initializationAttribute = new AnnotationDto(componentInstance.getInstanceAttribute());
    }
}
