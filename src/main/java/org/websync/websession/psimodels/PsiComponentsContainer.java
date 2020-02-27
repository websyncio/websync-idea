package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.ComponentsContainer;

import java.util.List;

public abstract class PsiComponentsContainer<T> extends PsiNamedTypeWrapper<T> implements ComponentsContainer {
    public PsiComponentsContainer(PsiClass psiClass) {
        super(psiClass);
    }

    public abstract List<ComponentInstance> getComponents();
}
