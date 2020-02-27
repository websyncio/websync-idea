package org.websync.sessionweb;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.websync.sessionweb.models.WebSession;
import org.websync.sessionweb.psimodels.PsiComponent;
import org.websync.sessionweb.psimodels.PsiPage;
import org.websync.sessionweb.psimodels.PsiWebSession;
import org.websync.sessionweb.psimodels.PsiWebsite;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.websync.jdi.JdiAttribute.JDI_JSITE;
import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;
import static org.websync.jdi.JdiElement.JDI_WEB_PAGE;

public class PsiWebSessionProvider implements WebSessionPovider {

    private Project project;
    private Collection<WebSession> cachedWebSessions;

    public PsiWebSessionProvider(Project project) {
        this.project = project;
    }

    @Override
    public Collection<WebSession> getWebSessions(boolean useCache) {
        if (cachedWebSessions == null || !useCache) {
            try {
                Collection<PsiWebsite> websites = getWebsites(project);
                Collection<PsiPage> pages = getPages(project);
                Collection<PsiComponent> components = getComponents(project);

                PsiWebSession webSession = new PsiWebSession(websites, components, pages);
                List<WebSession> webSessions = Arrays.asList(webSession);
                cacheWebSession(webSessions);
                return webSessions;
            } catch (Exception ex) {
                throw ex;
            }
        }
        return cachedWebSessions;
    }

    private Collection<PsiWebsite> getWebsites(Project project) {
        long startTime = System.nanoTime();

        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
        PsiElementFactoryImpl psiElementFactory = new PsiElementFactoryImpl(project);

        String JSiteAnnotation = JDI_JSITE.value.substring(JDI_JSITE.value.lastIndexOf(".") + 1);
        PsiClass psiClass = psiElementFactory.createAnnotationType(JSiteAnnotation);

        Collection<PsiWebsite> websites = AnnotatedElementsSearch.searchPsiClasses(psiClass, projectScope).findAll()
                .stream().map(c -> {
                    PsiWebsite website = new PsiWebsite(c);
                    website.Fill();
                    return website;
                }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        System.out.println(String.format("Time of getting website PSI classes = %s s.",
                (endTime - startTime) / 1000000));
        return websites;
    }

    private Collection<PsiPage> getPages(Project project) {
        long startTime = System.nanoTime();

        Collection<PsiClass> psiClasses = getDerivedPsiClasses(project, JDI_WEB_PAGE.value);

        Collection<PsiPage> pages = psiClasses.stream().map(c -> {
            PsiPage page = new PsiPage(c);
            page.Fill();
            return page;
        }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        System.out.println(String.format("Time of getting page PSI classes = %s s.",
                (endTime - startTime) / 1000000));
        return pages;
    }

    private Collection<PsiComponent> getComponents(Project project) {
        long startTime = System.nanoTime();

        Collection<PsiClass> psiClasses = getDerivedPsiClasses(project, JDI_UI_BASE_ELEMENT.value);

        Collection<PsiComponent> components = psiClasses.stream().map(c -> {
            PsiComponent component = new PsiComponent(c);
            component.Fill();
            return component;
        }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        System.out.println(String.format("Time of getting component PSI classes = %s s.",
                (endTime - startTime) / 1000000));
        return components;
    }

    private Collection<PsiClass> getDerivedPsiClasses(Project project, String classQualifiedName) {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);

        PsiClass psiClass = javaPsiFacade.findClass(classQualifiedName, allScope);
        Collection<PsiClass> classes = ClassInheritorsSearch.search(psiClass).findAll();
        return classes;
    }

    private void cacheWebSession(List<WebSession> webSessions) {
        cachedWebSessions = webSessions;
    }
}
