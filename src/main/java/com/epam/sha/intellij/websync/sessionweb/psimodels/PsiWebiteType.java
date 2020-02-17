package com.epam.sha.intellij.websync.sessionweb.psimodels;

import com.epam.sha.intellij.websync.sessionweb.models.WebsiteType;
import com.intellij.psi.PsiClass;

public class PsiWebiteType extends PsiNamedTypeWrapper<WebsiteType> implements WebsiteType {
    public PsiWebiteType(PsiClass psiClass) {
        super(psiClass);
    }
}
