package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import lombok.Getter;

public abstract class PsiNamedTypeWrapper<T> extends PsiModelWithId<T> {
    @Getter
    public PsiClass psiClass;

    public PsiNamedTypeWrapper(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public void fill() {
        id = psiClass.getQualifiedName();
    }
}
