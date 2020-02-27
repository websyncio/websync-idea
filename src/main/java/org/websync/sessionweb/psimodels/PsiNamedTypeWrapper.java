package org.websync.sessionweb.psimodels;

import com.intellij.psi.PsiClass;

public abstract class PsiNamedTypeWrapper<T> extends PsiModelWithId<T> {

    PsiClass psiClass;

    public PsiNamedTypeWrapper(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public void Fill() {
        id = psiClass.toString();
    }
}
