package org.websync.psi.models;

import com.intellij.psi.PsiClass;
import org.websync.models.PageType;

import static org.websync.frameworks.jdi.JdiElement.JDI_WEB_PAGE;

public class PsiPageType extends PsiComponentContainer<PageType> implements PageType {
    public String baseTypeId;

    @Override
    public String getBaseTypeId() {
        return baseTypeId;
    }

    @Override
    public String getUrl() {
        return null;
    }

    public PsiPageType(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
        PsiClass superClass = getPsiClass().getSuperClass();
        if(superClass!=null){
            String superClassName = superClass.getQualifiedName();
            if(!superClassName.equals(JDI_WEB_PAGE.className)) {
                baseTypeId = superClassName;
            }
        }
    }
}
