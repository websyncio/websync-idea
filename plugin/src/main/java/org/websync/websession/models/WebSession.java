package org.websync.websession.models;

import lombok.Getter;

import java.util.Map;

public abstract class WebSession {
    @Getter
    public Map<String, Page> pages;
    @Getter
    public Map<String, Website> websites;
    @Getter
    public Map<String, Component> components;
}
