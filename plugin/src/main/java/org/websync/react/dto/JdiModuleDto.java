package org.websync.react.dto;

import java.util.ArrayList;
import java.util.List;

public class JdiModuleDto {
    public String module;
    public List<WebsiteDto> webSites = new ArrayList<>();
    public List<PageTypeDto> pageTypes = new ArrayList<>();
    public List<ComponentTypeDto> componentTypes = new ArrayList<>();
}
