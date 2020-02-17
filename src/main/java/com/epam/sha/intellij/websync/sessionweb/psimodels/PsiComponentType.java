package com.epam.sha.intellij.websync.sessionweb.psimodels;

import com.epam.sha.intellij.websync.sessionweb.models.ComponentType;
import com.intellij.psi.PsiClass;

public class PsiComponentType extends PsiComponentsContainer<ComponentType> implements ComponentType {
    public PsiComponentType(PsiClass psiClass) {
        super(psiClass);
    }

    public void Fill() {
    }
}
