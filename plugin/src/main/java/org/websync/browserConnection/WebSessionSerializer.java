package org.websync.browserConnection;

import org.websync.websession.models.WebSession;

import java.util.Collection;

public interface WebSessionSerializer {
    String serialize(Collection<WebSession> webSessions);
    String serialize(WebSession webSession);

    Collection<WebSession> deserialize(String data);
}