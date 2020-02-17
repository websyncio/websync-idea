package com.epam.sha.intellij.websync.sessionweb;

import com.epam.sha.intellij.websync.sessionweb.models.SessionWeb;

import java.util.List;

interface SessionWebPovider {
    List<SessionWeb> getSessionWeb(boolean useCache);
}
