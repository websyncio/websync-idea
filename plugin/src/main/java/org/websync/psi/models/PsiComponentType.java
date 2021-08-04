package org.websync.psi.models;

import com.intellij.psi.PsiClass;
import org.websync.frameworks.jdi.JdiElement;
import org.websync.frameworks.jdi.JdiElementType;
import org.websync.frameworks.jdi.JdiFramework;
import org.websync.models.ComponentType;

import java.util.Optional;

import static org.websync.frameworks.jdi.JdiElement.JDI_UI_BASE_ELEMENT;

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
        PsiClass superClass = getPsiClass().getSuperClass();
        if(superClass!=null) {
            String superClassName = superClass.getQualifiedName();
            if (!superClassName.equals(JDI_UI_BASE_ELEMENT.className)) {
                baseComponentTypeId = superClassName;
            }
        }
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
    public String getBaseTypeId() {
        return baseComponentTypeId;
    }

    @Override
    public boolean getIsCustom() {
        return !isFrameworkElement;
    }
}