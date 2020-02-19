package org.websync.sessionweb;

import org.websync.sessionweb.models.SessionWeb;
import org.websync.sessionweb.psimodels.PsiComponentType;
import org.websync.sessionweb.psimodels.PsiPageType;
import org.websync.sessionweb.psimodels.PsiSessionWeb;
import org.websync.sessionweb.psimodels.PsiWebsite;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PsiSessionWebProvider implements SessionWebPovider {

    private Project project;
    private Collection<SessionWeb> cachedSessionWebs;

    public PsiSessionWebProvider(Project project) {
        this.project = project;
    }

    @Override
    public Collection<SessionWeb> getSessionWebs(boolean useCache) {
        if (cachedSessionWebs == null || !useCache) {
            try {
                Collection<PsiWebsite> websites = getWebsites(project);
                Collection<PsiPageType> pages = getPages(project);
                Collection<PsiComponentType> components = getComponents(project);

                PsiSessionWeb sessionWeb = new PsiSessionWeb(websites, components, pages);
                List<SessionWeb> sessionWebs = Arrays.asList(sessionWeb);
                cacheSessionWebs(sessionWebs);
                return sessionWebs;
            } catch (Exception ex) {
                throw ex;
            }
        }
        return cachedSessionWebs;
    }

    final public String JDI_JSITE = "JSite";
    final public String JDI_WEBPAGE = "com.epam.jdi.light.elements.composite.WebPage";
    final public String JDI_COMPONENT = "com.epam.jdi.light.elements.base.UIBaseElement";

    private Collection<PsiWebsite> getWebsites(Project project) {
        long startTime = System.currentTimeMillis();

        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
        PsiElementFactoryImpl psiElementFactory = new PsiElementFactoryImpl(project);
        PsiClass psiClass = psiElementFactory.createAnnotationType(JDI_JSITE);

        Collection<PsiWebsite> websites = AnnotatedElementsSearch.searchPsiClasses(psiClass, projectScope).findAll().stream().map(c -> {
            PsiWebsite website = new PsiWebsite(c);
            website.Fill();
            return website;
        }).collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Time of getting website PSI classes = %s s.",
                (endTime - startTime) / 1000));
        return websites;
    }

    private Collection<PsiPageType> getPages(Project project) {
        long startTime = System.currentTimeMillis();

        Collection<PsiClass> psiClasses = getDerivedClasses(project, JDI_WEBPAGE);

        Collection<PsiPageType> pageTypes = psiClasses.stream().map(c -> {
            PsiPageType page = new PsiPageType(c);
            page.Fill();
            return page;
        }).collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Time of getting page type PSI classes = %s s.",
                (endTime - startTime) / 1000));
        return pageTypes;
    }

    private Collection<PsiComponentType> getComponents(Project project) {
        long startTime = System.currentTimeMillis();

        Collection<PsiClass> psiClasses = getDerivedClasses(project, JDI_COMPONENT);

        Collection<PsiComponentType> componentTypes = psiClasses.stream().map(c -> {
            PsiComponentType component = new PsiComponentType(c);
            component.Fill();
            return component;
        }).collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Time of getting component type PSI classes = %s s.",
                (endTime - startTime) / 1000));
        return componentTypes;
    }

    private Collection<PsiClass> getDerivedClasses(Project project, String classQualifiedName) {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);

        PsiClass webPagePsiClass = javaPsiFacade.findClass(classQualifiedName, allScope);
        Collection<PsiClass> classes = ClassInheritorsSearch.search(webPagePsiClass).findAll();
        return classes;
    }

    private void cacheSessionWebs(List<SessionWeb> sessionWebs) {
        cachedSessionWebs = sessionWebs;
    }
}
