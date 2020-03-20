package org.websync.ember.dto;

import lombok.Getter;
import org.websync.websession.models.Component;

public class ComponentDto extends ComponentsContainerDto {

    @Getter
    private String baseComponentId;

    public ComponentDto(Component component) {
        super(component);
        baseComponentId = component.getBaseComponentId();
    }
}
