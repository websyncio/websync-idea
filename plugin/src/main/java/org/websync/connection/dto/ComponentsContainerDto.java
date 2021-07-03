package org.websync.connection.dto;

import org.websync.models.ComponentContainer;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentsContainerDto extends BaseDto {
    public List<ComponentInstanceDto> componentsInstances;
    public String baseType;

    public ComponentsContainerDto() {
    }

    public ComponentsContainerDto(ComponentContainer container) {
        super(container.getId());
        baseType = container.getBaseTypeId();
        componentsInstances = container.getComponentInstances()
                .stream().map(ComponentInstanceDto::new)
                .collect(Collectors.toList());
    }
}
