package com.epam.websync.sessionweb;

import com.epam.websync.sessionweb.models.SessionWeb;

import java.util.Collection;

interface SessionWebPovider {
    Collection<SessionWeb> getSessionWebs(boolean useCache);
}
