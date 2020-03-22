package org.websync.browserConnection;

import org.websync.websession.models.WebSession;

import java.util.Collection;
import java.util.List;

public interface WebSessionSerializer {
    String serialize(List<WebSession> webSessions);

    WebSession deserialize(String json);
}