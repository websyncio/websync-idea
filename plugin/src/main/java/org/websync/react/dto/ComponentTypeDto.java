package org.websync.react.dto;

import org.websync.websession.models.ComponentType;

public class ComponentTypeDto extends ComponentsContainerDto {
    public String baseComponentType;

    public ComponentTypeDto() {
    }

    public ComponentTypeDto(ComponentType component) {
        super(component);
        baseComponentType = component.getBaseComponentTypeId();
    }
}
