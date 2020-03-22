package org.websync.websession;

import org.websync.websession.models.WebSession;

import java.util.Collection;

public interface WebSessionPovider {
    WebSession getWebSession(boolean useCache);
}
