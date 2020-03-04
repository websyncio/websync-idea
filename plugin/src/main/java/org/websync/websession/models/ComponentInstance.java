package org.websync.websession.models;

import org.websync.websession.psimodels.PsiComponentInstance;

public interface ComponentInstance {
    String getAttributes();

    PsiComponentInstance.Locator getLocator();
}
