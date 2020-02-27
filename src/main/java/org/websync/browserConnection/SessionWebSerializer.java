package org.websync.browserConnection;

import org.websync.sessionweb.models.WebSession;

import java.util.Collection;

public interface SessionWebSerializer {
    String serialize(Collection<WebSession> webs);

    Collection<WebSession> deserialize(String data);
}
