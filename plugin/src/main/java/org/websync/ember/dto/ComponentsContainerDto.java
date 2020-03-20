package org.websync.ember.dto;

import lombok.Getter;
import org.websync.websession.models.ComponentContainer;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentsContainerDto extends BaseDto {
    @Getter
    private List<String> components;
    public ComponentsContainerDto(ComponentContainer container) {
        super(container.getId());
        components = container.getComponentInstances().stream().map(i -> i.getId()).collect(Collectors.toList());
    }
}
