package org.websync.sessionweb.psimodels;

import org.websync.sessionweb.models.Component;
import com.intellij.psi.PsiClass;

public class PsiComponent extends PsiComponentsContainer<Component> implements Component {
    public PsiComponent(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }
}
