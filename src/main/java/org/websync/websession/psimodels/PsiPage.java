package org.websync.websession.psimodels;

import com.intellij.psi.PsiClass;
import org.websync.websession.models.ComponentInstance;
import org.websync.websession.models.Page;

import java.util.List;

public class PsiPage extends PsiComponentsContainer<Page> implements Page {
    public PsiPage(PsiClass psiClass) {
        super(psiClass);
    }

    @Override
    public void Fill() {
        super.Fill();
    }

    @Override
    public List<ComponentInstance> getComponents() {
        return null;
    }
}
