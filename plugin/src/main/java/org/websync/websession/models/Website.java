package org.websync.websession.models;

import com.intellij.psi.PsiClass;

import java.util.List;

public interface Website {
    String getName(PsiClass psiClass);

    String getUrl(PsiClass psiClass);

    List<PageType> getPageTypes();
}
