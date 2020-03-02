package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.Page;

public class PsiPage extends PsiComponentContainer<Page> implements Page {
    public PsiPage(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }
}
