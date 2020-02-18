package com.epam.websync.sessionweb;

import com.epam.websync.sessionweb.models.SessionWeb;

import java.util.List;

interface SessionWebPovider {
    List<SessionWeb> getSessionWeb(boolean useCache);
}
