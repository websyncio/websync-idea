package org.websync.websession.models;

import org.websync.websession.psimodels.PsiComponentInstance;

public interface ComponentInstance {
    PsiComponentInstance.Locator getLocator();
}
