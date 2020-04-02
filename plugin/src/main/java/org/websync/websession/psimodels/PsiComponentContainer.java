package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import org.websync.jdi.JdiElement;
import org.websync.websession.models.ComponentContainer;
import org.websync.websession.models.ComponentInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.websync.jdi.JdiAttribute.JDI_BY_TEXT;
import static org.websync.jdi.JdiAttribute.JDI_CSS;
import static org.websync.jdi.JdiAttribute.JDI_JDROPDOWN;
import static org.websync.jdi.JdiAttribute.JDI_JMENU;
import static org.websync.jdi.JdiAttribute.JDI_JTABLE;
import static org.websync.jdi.JdiAttribute.JDI_UI;
import static org.websync.jdi.JdiAttribute.JDI_WITH_TEXT;
import static org.websync.jdi.JdiAttribute.JDI_XPATH;

public abstract class PsiComponentContainer<T> extends PsiNamedTypeWrapper<T> implements ComponentContainer {
    public static final List<String> INITIALIZATION_ATTRIBUTES = Arrays.asList(JDI_BY_TEXT.value, JDI_CSS.value, JDI_JDROPDOWN.value, JDI_JMENU.value, JDI_JTABLE.value, JDI_UI.value,
            JDI_WITH_TEXT.value, JDI_XPATH.value);

    private List<ComponentInstance> componentInstances;

    public List<ComponentInstance> getComponentInstances() {
        return componentInstances;
    }

    public PsiComponentContainer(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();

        this.componentInstances = new ArrayList<>();
        List<PsiField> fieldsList = Arrays.asList(psiClass.getFields());

        fieldsList.stream().forEach(f -> {
            boolean isElement = Arrays.asList(f.getType().getSuperTypes())
                    .stream()
                    .anyMatch(s -> {
                        PsiClass c = PsiUtil.resolveClassInType(s);
                        return InheritanceUtil.isInheritor(c, JdiElement.JDI_UI_BASE_ELEMENT.value);
                    });

            if (isElement) {
                PsiComponentInstance psiComponentInstance = new PsiComponentInstance(getId(), f);
                if (INITIALIZATION_ATTRIBUTES.stream().anyMatch(p -> (p.contains(psiComponentInstance.getAttributeInstance().getCodeReferenceElement())))) {
                    psiComponentInstance.fill();
                    this.componentInstances.add(psiComponentInstance);
                }
            }
        });
    }
}
