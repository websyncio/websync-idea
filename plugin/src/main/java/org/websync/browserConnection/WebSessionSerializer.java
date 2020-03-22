package org.websync.browserConnection;

import org.websync.websession.models.WebSession;

import java.util.Collection;

public interface WebSessionSerializer {
    String serialize(WebSession webSession);

    WebSession deserialize(String json);
}