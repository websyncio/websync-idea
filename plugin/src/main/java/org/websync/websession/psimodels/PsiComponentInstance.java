package org.websync.websession.psimodels;

import com.intellij.psi.PsiField;
import org.websync.websession.models.ComponentInstance;

public class PsiComponentInstance implements ComponentInstance {
    private PsiField psiFiled;

    public PsiComponentInstance(PsiField psiFiled) {
        this.psiFiled = psiFiled;
    }

    public void Fill() {
    }
}
