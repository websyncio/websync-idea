package org.websync.psi.models;

import com.intellij.psi.PsiClass;
import org.websync.models.WebSite;

public class PsiWebsite extends PsiPageContainer<WebSite> implements WebSite {
    public PsiWebsite(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public String getBaseWebsiteId() {
        return null;
    }

    @Override
    public String getUrl() {
        // filter JSite annotations
        return getPsiClass().getAnnotations()[0].getParameterList().getAttributes()[0].getLiteralValue();
    }

    @Override
    public void fill() {
//        getBaseWebsiteId();
//        getUrl();
        super.fill();
    }

    @Override
    public boolean updatePageInstance(String oldName, String newName) {
        // here

        return false;
    }
}