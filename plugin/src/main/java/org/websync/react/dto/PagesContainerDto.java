package org.websync.react.dto;

import org.websync.websession.models.PageContainer;

import java.util.List;
import java.util.stream.Collectors;

public class PagesContainerDto extends BaseDto {
    public List<PageInstanceDto> pageInstances;

    public PagesContainerDto() {
    }

    public PagesContainerDto(PageContainer container) {
        super(container.getId());
        pageInstances = container.getPageInstances()
                .stream().map(PageInstanceDto::new)
                .collect(Collectors.toList());
    }
}
