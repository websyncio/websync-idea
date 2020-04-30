package org.websync.websession.models;

import lombok.Getter;

import java.util.Map;

public abstract class JdiModule {
    @Getter
    public Map<String, PageType> pageTypes;
    @Getter
    public Map<String, Website> websites;
    @Getter
    public Map<String, ComponentType> componentTypes;
    @Getter
    public String name;
}
