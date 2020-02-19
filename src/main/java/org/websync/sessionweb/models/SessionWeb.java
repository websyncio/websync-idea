package org.websync.sessionweb.models;

import lombok.Getter;

import java.util.Map;

public abstract class SessionWeb {
    @Getter
    protected Map<String, PageType> pageTypes;
    @Getter
    protected Map<String, Website> websites;
    @Getter
    protected Map<String, ComponentType> componentTypes;
}
