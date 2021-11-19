package org.websync.psi.models;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.websync.models.ComponentContainer;
import org.websync.models.ComponentInstance;
import org.websync.utils.PsiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.websync.frameworks.jdi.JdiAttribute.*;

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
            boolean isAnnotated = false;
            PsiComponentInstance psiComponentInstance = new PsiComponentInstance(getId(), field, i);
            AnnotationInstance annotationInstance = psiComponentInstance.getAttributeInstance();
            if(annotationInstance!=null) {
                String attributeClassName = psiComponentInstance.getAttributeInstance().getCodeReferenceElement();
                isAnnotated = INITIALIZATION_ATTRIBUTES.stream().anyMatch(p -> (p.contains(attributeClassName)));
            }
            boolean isElement = PsiUtils.isComponent(field.getType());
//                    Arrays.asList(field.getType().getSuperTypes())
//                    .stream()
//                    .anyMatch(s -> {
//                        PsiClass c = PsiUtil.resolveClassInType(s);
//                        return InheritanceUtil.isInheritor(c, JdiElement.JDI_UI_BASE_ELEMENT.className);
//                    });

            if (isElement || isAnnotated) {
                psiComponentInstance.fill();
                this.componentInstances.add(psiComponentInstance);
            }
        });
    }
}
