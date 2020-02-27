package org.websync.sessionweb.psimodels;

import org.websync.sessionweb.models.Page;
import com.intellij.psi.PsiClass;

public class PsiPage extends PsiComponentsContainer<Page> implements Page {
    public PsiPage(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }
}
