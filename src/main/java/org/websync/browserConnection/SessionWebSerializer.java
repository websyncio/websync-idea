package org.websync.browserConnection;

import org.websync.sessionweb.models.SessionWeb;

import java.util.Collection;

public interface SessionWebSerializer {
    String serialize(Collection<SessionWeb> webs);

    Collection<SessionWeb> deserialize(String data);
}
