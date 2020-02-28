package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.Component;

import java.util.List;

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

    @Override
    public List<Component> getComponents() {
        return null;
    }
}
