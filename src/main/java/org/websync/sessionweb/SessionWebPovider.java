package org.websync.sessionweb;

import org.websync.sessionweb.models.SessionWeb;

import java.util.Collection;

interface SessionWebPovider {
    Collection<SessionWeb> getSessionWebs(boolean useCache);
}
