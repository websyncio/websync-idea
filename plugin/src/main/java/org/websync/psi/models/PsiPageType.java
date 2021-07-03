package org.websync.psi.models;

import com.intellij.psi.PsiClass;
import org.websync.models.PageType;

public class PsiPageType extends PsiComponentContainer<PageType> implements PageType {
    @Override
    public String getBaseTypeId() {
        return null;
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
    }
}
