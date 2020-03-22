package org.websync.react.dto;

import org.websync.websession.models.ComponentContainer;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentsContainerDto extends BaseDto {
    public List<ComponentInstanceDto> componentsInstances;

    public ComponentsContainerDto(ComponentContainer container) {
        super(container.getId());
        componentsInstances = container.getComponentInstances()
                .stream().map(i -> new ComponentInstanceDto(i))
                .collect(Collectors.toList());
    }
}
