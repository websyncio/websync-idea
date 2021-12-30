package org.websync.models;

import lombok.Getter;
import org.websync.connection.dto.JdiProjectDto;

import java.util.Map;

public abstract class SeleniumProject {
    @Getter
    public Map<String, PageType> pageTypes;
    @Getter
    public Map<String, WebSite> websites;
    @Getter
    public Map<String, ComponentType> componentTypes;
    @Getter
    public String name;

    public abstract JdiProjectDto getDto();
}
