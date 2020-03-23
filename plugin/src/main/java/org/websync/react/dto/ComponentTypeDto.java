package org.websync.react.dto;

import org.websync.websession.models.ComponentType;

public class ComponentTypeDto extends ComponentsContainerDto {
    public String baseComponentTypeId;

    public ComponentTypeDto(ComponentType component) {
        super(component);
        baseComponentTypeId = component.getBaseComponentTypeId();
    }
}
