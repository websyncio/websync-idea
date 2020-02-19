package org.websync.sessionweb.psimodels;

import com.intellij.psi.PsiClass;

public abstract class PsiComponentsContainer<T> extends PsiNamedTypeWrapper<T> {
    public PsiComponentsContainer(PsiClass psiClass) {
        super(psiClass);
    }
}
