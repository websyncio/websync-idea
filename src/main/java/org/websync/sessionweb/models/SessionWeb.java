package org.websync.sessionweb.models;

import lombok.Getter;

import java.util.Map;

public abstract class SessionWeb {
    @Getter
    protected Map<String, Page> pageTypes;
    @Getter
    protected Map<String, Website> websites;
    @Getter
    protected Map<String, Component> componentTypes;
}
