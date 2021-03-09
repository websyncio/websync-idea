package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.jdi.JdiElement;
import org.websync.jdi.JdiElementType;
import org.websync.jdi.JdiFramework;
import org.websync.websession.models.ComponentType;

import java.util.Optional;

import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;

public class PsiComponentType extends PsiComponentContainer<ComponentType> implements ComponentType {
    private String baseComponentTypeId;
    public Boolean isFrameworkElement;
    public JdiElementType frameworkElementType;

    public PsiComponentType(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
        baseComponentTypeId = getId().equals(JDI_UI_BASE_ELEMENT.className) ?
                null :
                getPsiClass().getSuperClass().getQualifiedName();
        this.isFrameworkElement = getId().startsWith(JdiFramework.ELEMENTS_NAMESPACE.value)
                || getId().startsWith(JdiFramework.HTML_ELEMENTS_NAMESPACE.value);
        if (this.isFrameworkElement) {
            Optional<JdiElement> elementType = JdiElement.findElement(getId());
            this.frameworkElementType = elementType.isPresent() ?
                    elementType.get().type :
                    JdiElementType.Undefined;
        }
    }

    @Override
    public String getBaseComponentTypeId() {
        return baseComponentTypeId;
    }

    @Override
    public boolean getIsCustom() {
        return !isFrameworkElement;
    }
}