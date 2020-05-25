package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.Website;

public class PsiWebsite extends PsiPageContainer<Website> implements Website {
    public PsiWebsite(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public String getBaseWebsiteId() {
        return getUrl();
    }

    @Override
    public String getUrl() {
        return getPsiClass().getAnnotations()[0].getParameterList().getAttributes()[0].getLiteralValue();
    }

    @Override
    public void fill() {
        getBaseWebsiteId();
        getUrl();
        super.fill();
    }

    @Override
    public boolean updatePageInstance(String oldName, String newName) {
        return false;
    }
}