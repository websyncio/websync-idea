package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.jdi.JdiFramework;
import org.websync.websession.models.ComponentType;

import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;

public class PsiComponentType extends PsiComponentContainer<ComponentType> implements ComponentType {
    private String baseComponentTypeId;
    private Boolean isCustom;

    public PsiComponentType(PsiClass psiClass, boolean isCustom) {
        super(psiClass);
        this.isCustom = isCustom;
    }

    @Override
    public void fill() {
        super.fill();
        baseComponentTypeId = getId().equals(JDI_UI_BASE_ELEMENT.className) ?
                null :
                getPsiClass().getSuperClass().getQualifiedName();
        this.isCustom = !getId().startsWith(JdiFramework.ELEMENTS_NAMESPACE.value);
    }

    @Override
    public String getBaseComponentTypeId() {
        return baseComponentTypeId;
    }

    @Override
    public boolean getIsCustom() {
        return isCustom;
    }
}
