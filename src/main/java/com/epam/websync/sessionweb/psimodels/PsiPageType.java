package com.epam.websync.sessionweb.psimodels;

import com.epam.websync.sessionweb.models.PageType;
import com.intellij.psi.PsiClass;

public class PsiPageType extends PsiComponentsContainer<PageType> implements PageType {
    public PsiPageType(PsiClass psiClass) {
        super(psiClass);
    }

    public void Fill() {
    }
}
