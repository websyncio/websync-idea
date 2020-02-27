package org.websync.sessionweb;

import org.websync.sessionweb.models.WebSession;

import java.util.Collection;

interface WebSessionPovider {
    Collection<WebSession> getWebSessions(boolean useCache);
}
