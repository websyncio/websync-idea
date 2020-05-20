package org.websync.websession.psimodels;

import com.epam.jdi.light.elements.pageobjects.annotations.JSite;
import com.intellij.psi.PsiClass;
import org.websync.websession.models.PageType;
import org.websync.websession.models.Website;

import java.util.List;

public class PsiWebsite extends PsiNamedTypeWrapper<Website> implements Website {
    public PsiWebsite(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
    }

    @Override
    public String getName(PsiClass psiClass) {
        return psiClass.getQualifiedName();
    }

    @Override
    public String getUrl(PsiClass psiClass) {
//        JSite column = psiClass.getAnnotation(JSite.class);
//        if (column != null)
//            System.out.println(column.columnName());
        return null;
    }

    @Override
    public List<PageType> getPageTypes() {
        return null;
    }
}
