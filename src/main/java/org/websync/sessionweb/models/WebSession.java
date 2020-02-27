package org.websync.sessionweb.models;

import lombok.Getter;

import java.util.Map;

public abstract class WebSession {
    @Getter
    protected Map<String, Page> pages;
    @Getter
    protected Map<String, Website> websites;
    @Getter
    protected Map<String, Component> components;
}
