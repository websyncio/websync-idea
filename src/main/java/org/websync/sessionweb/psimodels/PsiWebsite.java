package org.websync.sessionweb.psimodels;

import org.websync.sessionweb.models.Website;
import com.intellij.psi.PsiClass;

public class PsiWebsite extends PsiNamedTypeWrapper<Website> implements Website {
    public PsiWebsite(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }
}
