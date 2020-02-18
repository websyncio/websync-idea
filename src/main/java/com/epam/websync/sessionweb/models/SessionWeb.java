package com.epam.websync.sessionweb.models;

import java.util.Map;

public abstract class SessionWeb {
    protected Map<String, PageType> pageTypes;
    protected Map<String, WebsiteType> websiteTypes;
    protected Map<String, ComponentType> componentTypes;
}
