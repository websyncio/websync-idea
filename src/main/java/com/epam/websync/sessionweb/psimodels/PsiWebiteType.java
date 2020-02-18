package com.epam.websync.sessionweb.psimodels;

import com.epam.websync.sessionweb.models.Website;
import com.intellij.psi.PsiClass;

public class PsiWebiteType extends PsiNamedTypeWrapper<Website> implements Website {
    public PsiWebiteType(PsiClass psiClass) {
        super(psiClass);
    }
}
