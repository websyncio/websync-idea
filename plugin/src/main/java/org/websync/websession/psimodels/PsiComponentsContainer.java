package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import org.websync.jdi.JdiElement;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.ComponentsContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PsiComponentsContainer<T> extends PsiNamedTypeWrapper<T> implements ComponentsContainer {

    List<ComponentInstance> componentInstances = new ArrayList<>();

    public PsiComponentsContainer(PsiClass psiClass) {
        super(psiClass);
    }

    public List<ComponentInstance> getComponentInstances() {
        return componentInstances;
    }

    @Override
    public void Fill() {
        super.Fill();

        List<PsiField> fieldsList = Arrays.asList(psiClass.getFields());

        fieldsList.stream().forEach(f -> {
            boolean isElement = Arrays.asList(f.getType().getSuperTypes())
                    .stream()
                    .anyMatch(s -> {
                        PsiClass c = PsiUtil.resolveClassInType(s);
                        return InheritanceUtil.isInheritor(c, JdiElement.JDI_UI_BASE_ELEMENT.value);
                    });
            if (isElement) {
                PsiComponentInstance psiComponentInstance = new PsiComponentInstance(f);
                psiComponentInstance.Fill();
                componentInstances.add(psiComponentInstance);
            }
        });
    }
}
