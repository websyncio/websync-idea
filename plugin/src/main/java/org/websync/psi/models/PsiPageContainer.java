package org.websync.psi.models;


import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import org.websync.frameworks.jdi.JdiElement;
import org.websync.models.PageContainer;
import org.websync.models.PageInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.websync.frameworks.jdi.JdiAttribute.JDI_JSITE;
import static org.websync.frameworks.jdi.JdiAttribute.JDI_TITLE;
import static org.websync.frameworks.jdi.JdiAttribute.JDI_URL;

public class PsiPageContainer<T> extends PsiNamedTypeWrapper<T> implements PageContainer {
    public static final List<String> INITIALIZATION_ATTRIBUTES = Arrays.asList(JDI_URL.className, JDI_TITLE.className, JDI_JSITE.className);

    private List<PageInstance> pageInstances;

    public List<PageInstance> getPageInstances() {
        return pageInstances;
    }

    public PsiPageContainer(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public boolean updatePageInstance(String oldName, String newName) {
        Optional<PageInstance> found = pageInstances.stream().filter(c -> c.getName().equals(oldName)).findFirst();
        found.ifPresent(c -> ((PsiPageInstance) c).setFieldName(newName));
        return found.isPresent();
    }

    @Override
    public void fill() {
        super.fill();

        this.pageInstances = new ArrayList<>();
        List<PsiField> fieldsList = Arrays.asList(getPsiClass().getFields());

        fieldsList.stream().forEach(f -> {
            boolean isElement = Arrays.asList(f.getType().getSuperTypes())
                    .stream()
                    .anyMatch(s -> {
                        PsiClass c = PsiUtil.resolveClassInType(s);
                        return InheritanceUtil.isInheritor(c, JdiElement.JDI_WEB_PAGE.className);
                    });

            if (isElement) {
                PsiPageInstance psiPageInstance = new PsiPageInstance(getId(), f);
                if (INITIALIZATION_ATTRIBUTES.stream().anyMatch(p -> (p.contains(psiPageInstance.getAttributeInstance().getCodeReferenceElement())))) {
                    psiPageInstance.fill();
                    this.pageInstances.add(psiPageInstance);
                }
            }
        });
    }
}
