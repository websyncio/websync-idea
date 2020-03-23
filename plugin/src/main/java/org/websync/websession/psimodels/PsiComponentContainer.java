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

public abstract class PsiComponentContainer<T> extends PsiNamedTypeWrapper<T> implements ComponentContainer {
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
                psiComponentInstance.fill();
                this.componentInstances.add(psiComponentInstance);
            }
        });
    }
}
