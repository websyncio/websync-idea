package com.epam.websync.sessionweb.psimodels;

import com.epam.websync.sessionweb.models.Website;
import com.intellij.psi.PsiClass;

public class PsiWebsite extends PsiNamedTypeWrapper<Website> implements Website {
    public PsiWebsite(PsiClass psiClass) {
        super(psiClass);
    }
}
