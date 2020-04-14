package org.websync.react.dto;

import java.util.ArrayList;
import java.util.List;

public class WebSessionDto {
    public String module;
    public List<WebsiteDto> websites = new ArrayList<>();
    public List<PageTypeDto> pages = new ArrayList<>();
    public List<ComponentTypeDto> components = new ArrayList<>();
}
