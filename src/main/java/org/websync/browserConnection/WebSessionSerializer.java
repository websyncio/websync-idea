package org.websync.browserConnection;

import org.websync.websession.models.WebSession;

import java.util.Collection;

public interface WebSessionSerializer {
    String serialize(Collection<WebSession> webs);

    Collection<WebSession> deserialize(String data);
}
