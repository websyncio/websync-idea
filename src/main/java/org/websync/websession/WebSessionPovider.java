package org.websync.websession;

import org.websync.websession.models.WebSession;

import java.util.Collection;

interface WebSessionPovider {
    Collection<WebSession> getWebSessions(boolean useCache);
}
