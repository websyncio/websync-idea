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
import java.util.Optional;
import java.util.stream.IntStream;

import static org.websync.jdi.JdiAttribute.*;

public abstract class PsiComponentContainer<T> extends PsiNamedTypeWrapper<T> implements ComponentContainer {
    public static final List<String> INITIALIZATION_ATTRIBUTES = Arrays.asList(JDI_BY_TEXT.className, JDI_CSS.className, JDI_JDROPDOWN.className, JDI_JMENU.className, JDI_JTABLE.className, JDI_UI.className,
            JDI_WITH_TEXT.className, JDI_XPATH.className);

    private List<ComponentInstance> componentInstances;

    public List<ComponentInstance> getComponentInstances() {
        return componentInstances;
    }

    public PsiComponentContainer(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public boolean updateComponentInstance(String oldName, String newName) {
        Optional<ComponentInstance> found = componentInstances.stream().filter(c -> c.getName().equals(oldName)).findFirst();
        found.ifPresent(c -> ((PsiComponentInstance) c).setFieldName(newName));
        return found.isPresent();
    }

    @Override
    public void fill() {
        super.fill();

        this.componentInstances = new ArrayList<>();
        List<PsiField> fieldsList = Arrays.asList(getPsiClass().getFields());
        IntStream.range(0, fieldsList.size()).forEach(i -> {
            PsiField field = fieldsList.get(i);
            boolean isElement = Arrays.asList(field.getType().getSuperTypes())
                    .stream()
                    .anyMatch(s -> {
                        PsiClass c = PsiUtil.resolveClassInType(s);
                        return InheritanceUtil.isInheritor(c, JdiElement.JDI_UI_BASE_ELEMENT.className);
                    });

            if (isElement) {
                PsiComponentInstance psiComponentInstance = new PsiComponentInstance(getId(), field, i);
                if (INITIALIZATION_ATTRIBUTES.stream().anyMatch(p -> (p.contains(psiComponentInstance.getAttributeInstance().getCodeReferenceElement())))) {
                    psiComponentInstance.fill();
                    this.componentInstances.add(psiComponentInstance);
                }
            }
        });
    }
}
