package org.websync.sessionweb.psimodels;

import org.websync.sessionweb.models.Component;
import com.intellij.psi.PsiClass;

import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;

public class PsiComponent extends PsiComponentsContainer<Component> implements Component {

    String baseComponentId;

    public PsiComponent(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
        baseComponentId = psiClass.getQualifiedName().equals(JDI_UI_BASE_ELEMENT.value) ? null : psiClass.getSuperClass().getQualifiedName();
    }

    @Override
    public String getBaseComponentId() {
        return baseComponentId;
    }
}
