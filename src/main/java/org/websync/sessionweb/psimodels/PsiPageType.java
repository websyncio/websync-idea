package org.websync.sessionweb.psimodels;

import org.websync.sessionweb.models.PageType;
import com.intellij.psi.PsiClass;

public class PsiPageType extends PsiComponentsContainer<PageType> implements PageType {
    public PsiPageType(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }
}
