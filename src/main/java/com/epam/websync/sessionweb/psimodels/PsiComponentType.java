package com.epam.websync.sessionweb.psimodels;

import com.epam.websync.sessionweb.models.ComponentType;
import com.intellij.psi.PsiClass;

public class PsiComponentType extends PsiComponentsContainer<ComponentType> implements ComponentType {
    public PsiComponentType(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }
}
