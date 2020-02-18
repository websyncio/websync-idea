package com.epam.websync.sessionweb.psimodels;

import com.epam.websync.sessionweb.models.WebsiteType;
import com.intellij.psi.PsiClass;

public class PsiWebiteType extends PsiNamedTypeWrapper<WebsiteType> implements WebsiteType {
    public PsiWebiteType(PsiClass psiClass) {
        super(psiClass);
    }
}
