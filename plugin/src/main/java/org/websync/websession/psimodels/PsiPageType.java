package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.PageType;

public class PsiPageType extends PsiComponentContainer<PageType> implements PageType {
    @Override
    public String getBasePageTypeId() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }

    public PsiPageType(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
    }
}
