package org.websync.react.dto;

import org.websync.websession.models.ComponentType;

public class ComponentTypeDto extends ComponentsContainerDto {
    public boolean isCustom;

    public ComponentTypeDto() {
    }

    public ComponentTypeDto(ComponentType component) {
        super(component);
        isCustom = component.getIsCustom();
    }
}
