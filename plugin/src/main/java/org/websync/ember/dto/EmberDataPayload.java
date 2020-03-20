package org.websync.ember.dto;

import java.util.ArrayList;
import java.util.List;

public class EmberDataPayload {
    public List<WebsiteDto> websites = new ArrayList<>();
    public List<PageDto> pages = new ArrayList<>();
    public List<ComponentDto> components = new ArrayList<>();
    public List<ComponentInstanceDto> componentInstances = new ArrayList<>();
}
