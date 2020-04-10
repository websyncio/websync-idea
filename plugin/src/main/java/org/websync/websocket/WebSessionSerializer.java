package org.websync.websocket;

import org.websync.websession.models.WebSession;

import java.util.List;

public interface WebSessionSerializer {
    String serialize(List<WebSession> webSessions);
    String serialize(WebSession webSession);
    WebSession deserialize(String json);
}