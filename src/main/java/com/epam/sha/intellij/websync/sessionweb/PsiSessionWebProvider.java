package com.epam.sha.intellij.websync.sessionweb;

import com.epam.sha.intellij.websync.sessionweb.models.SessionWeb;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiComponentType;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiPageType;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiSessionWeb;
import com.epam.sha.intellij.websync.sessionweb.psimodels.PsiWebiteType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PsiSessionWebProvider implements SessionWebPovider {

    private Project project;
    private List<SessionWeb> cachedSessionWebs;

    public PsiSessionWebProvider(Project project) {
        this.project = project;
    }

    @Override
    public List<SessionWeb> getSessionWeb(boolean useCache) {
        if (cachedSessionWebs == null || !useCache) {
            try {
                List<PsiWebiteType> websites = getWebsites(project);
                List<PsiPageType> pages = getPages(project);
                List<PsiComponentType> components = getComponents(project);

                PsiSessionWeb sessionWeb = new PsiSessionWeb(websites, components, pages);
                List<SessionWeb> sessionWebs = Arrays.asList(sessionWeb);
                cacheSesionWebs(sessionWebs);
                return sessionWebs;
            } catch (Exception ex) {
                throw ex;
            }
        }
        return cachedSessionWebs;
    }

    private List<PsiWebiteType> getWebsites(Project project) {
        return null;
    }

    final public String JDI_WEBPAGE = "com.epam.jdi.light.elements.composite.WebPage";
    final public String JDI_COMPONENT = "com.epam.jdi.light.elements.base.UIBaseElement";

    private List<PsiPageType> getPages(Project project) {
        List<PsiClass> derivedClasses = getDerivedClasses(project, JDI_WEBPAGE);
        return derivedClasses.stream().map(dc -> {
            PsiPageType page = new PsiPageType(dc);
            page.Fill();
            return page;
        }).collect(Collectors.toList());
    }

    private List<PsiComponentType> getComponents(Project project) {
        List<PsiClass> derivedClasses = getDerivedClasses(project, JDI_COMPONENT);
        return derivedClasses.stream().map(dc -> {
            PsiComponentType component = new PsiComponentType(dc);
            component.Fill();
            return component;
        }).collect(Collectors.toList());
    }

    private List<PsiClass> getDerivedClasses(Project project, String classQualifiedName) {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);

        PsiClass webPagePsiClass = javaPsiFacade.findClass(classQualifiedName, allScope);
        List<PsiClass> classes = ClassInheritorsSearch.search(webPagePsiClass).findAll()
                .stream().collect(Collectors.toList());
        return classes;
    }

    private void cacheSesionWebs(List<SessionWeb> sessionWebs) {
        cachedSessionWebs = sessionWebs;
    }
}
