package org.websync.react.dto;

import java.util.ArrayList;
import java.util.List;

public class ReactDataPayload {
    public List<WebsiteDto> websites = new ArrayList<>();
    public List<PageTypeDto> pages = new ArrayList<>();
    public List<ComponentTypeDto> components = new ArrayList<>();
//    public List<ComponentInstanceDto> componentInstances = new ArrayList<>();
}
