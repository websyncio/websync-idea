package org.websync.psi.models;

import com.intellij.psi.PsiClass;
import lombok.Getter;

public abstract class PsiNamedTypeWrapper<T> extends PsiModelWithId<T> {
    @Getter
    private final PsiClass psiClass;

    public PsiNamedTypeWrapper(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    public void fill() {
        setId(psiClass.getQualifiedName());
    }
}
