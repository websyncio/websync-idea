package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.ComponentType;

import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;

public class PsiComponentType extends PsiComponentContainer<ComponentType> implements ComponentType {
    private String baseComponentTypeId;

    public PsiComponentType(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
        baseComponentTypeId = getId().equals(JDI_UI_BASE_ELEMENT.value) ?
                null :
                getPsiClass().getSuperClass().getQualifiedName();
    }

    @Override
    public String getBaseComponentTypeId() {
        return baseComponentTypeId;
    }
}
