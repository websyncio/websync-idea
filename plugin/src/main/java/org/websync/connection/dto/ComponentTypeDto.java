package org.websync.connection.dto;

import org.websync.models.ComponentType;

public class ComponentTypeDto extends ComponentsContainerDto {
    public boolean isCustom;

    public ComponentTypeDto() {
    }

    public ComponentTypeDto(ComponentType component) {
        super(component);
        isCustom = component.getIsCustom();
    }
}
