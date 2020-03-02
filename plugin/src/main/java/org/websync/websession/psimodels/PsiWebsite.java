package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.Website;

public class PsiWebsite extends PsiNamedTypeWrapper<Website> implements Website {
    public PsiWebsite(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void fill() {
        super.fill();
    }
}
