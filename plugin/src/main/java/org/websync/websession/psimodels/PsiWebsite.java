package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.WebSite;

public class PsiWebsite extends PsiPageContainer<WebSite> implements WebSite {
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