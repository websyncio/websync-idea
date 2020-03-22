package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.ComponentType;

import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;

public class PsiComponent extends PsiComponentContainer<ComponentType> implements ComponentType {

    String baseComponentId;

    public PsiComponent(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
        baseComponentId = psiClass.getQualifiedName().equals(JDI_UI_BASE_ELEMENT.value) ? null : psiClass.getSuperClass().getQualifiedName();
    }

    @Override
    public String getBaseComponentTypeId() {
        return baseComponentId;
    }
}
