package org.websync.connection.dto;

import java.util.ArrayList;
import java.util.List;

public class JdiProjectDto {
    public String project;
    public List<WebsiteDto> webSites = new ArrayList<>();
    public List<PageTypeDto> pageTypes = new ArrayList<>();
    public List<ComponentTypeDto> componentTypes = new ArrayList<>();
}
