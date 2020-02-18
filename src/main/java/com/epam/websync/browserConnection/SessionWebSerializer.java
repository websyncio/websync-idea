package com.epam.websync.browserConnection;

import com.epam.websync.sessionweb.models.SessionWeb;

import java.util.Collection;

public interface SessionWebSerializer {
    String serialize(Collection<SessionWeb> webs);

    Collection<SessionWeb> deserialize(String data);
}
