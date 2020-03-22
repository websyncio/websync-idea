package org.websync.websession;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponent;
import org.websync.websession.psimodels.PsiPage;
import org.websync.websession.psimodels.PsiWebSession;
import org.websync.websession.psimodels.PsiWebsite;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.websync.jdi.JdiAttribute.JDI_JSITE;
import static org.websync.jdi.JdiElement.JDI_UI_BASE_ELEMENT;
import static org.websync.jdi.JdiElement.JDI_WEB_PAGE;

public class PsiWebSessionProvider implements WebSessionPovider {

    private Project project;
    private WebSession cachedWebSession;

    public PsiWebSessionProvider(Project project) {
        this.project = project;
    }

//    public WebSession getWebSession(Project project) {
//        Collection<WebSession> sessions = getWebSessions(true);
//        WebSession session = sessions.stream().findFirst().get();
//        return session;
//    }

    @Override
    public WebSession getWebSession(boolean useCache) {
        if (cachedWebSession == null || !useCache) {
            try {
                Collection<PsiWebsite> websites = getWebsites(project);
                Collection<PsiPage> pages = getPages(project);
                Collection<PsiComponent> components = getComponents(project);

                PsiWebSession webSession = new PsiWebSession(websites, components, pages);
                cacheWebSession(webSession);
                return webSession;
            } catch (Exception ex) {
                throw ex;
            }
        }
        return cachedWebSession;
    }

    private Collection<PsiWebsite> getWebsites(Project project) {
        long startTime = System.nanoTime();

        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
        PsiElementFactoryImpl psiElementFactory = new PsiElementFactoryImpl(project);

        PsiClass psiClass = psiElementFactory.createAnnotationType(JDI_JSITE.getShortName());

        Collection<PsiWebsite> websites = AnnotatedElementsSearch.searchPsiClasses(psiClass, projectScope).findAll()
                .stream().map(c -> {
                    PsiWebsite website = new PsiWebsite(c);
                    website.fill();
                    return website;
                }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        System.out.println(String.format("Getting website PSI classes. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return websites;
    }

    private Collection<PsiPage> getPages(Project project) {
        long startTime = System.nanoTime();
        Collection<PsiClass> psiClasses = getDerivedPsiClasses(project, JDI_WEB_PAGE.value);
        Collection<PsiPage> pages = psiClasses.stream().map(c -> {
            PsiPage page = new PsiPage(c);
            page.fill();
            return page;
        }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        System.out.println(String.format("Getting page PSI classes. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return pages;
    }

    private Collection<PsiComponent> getComponents(Project project) {
        long startTime = System.nanoTime();

        Collection<PsiClass> psiClasses = getDerivedPsiClasses(project, JDI_UI_BASE_ELEMENT.value);
        Collection<PsiComponent> components = psiClasses.stream().map(c -> {
            PsiComponent component = new PsiComponent(c);
            component.fill();
            return component;
        }).collect(Collectors.toList());

        long endTime = System.nanoTime();
        System.out.println(String.format("Getting component PSI classes. Time = %.3f s.",
                (double) (endTime - startTime) / 1000000000));
        return components;
    }

    private Collection<PsiClass> getDerivedPsiClasses(Project project, String classQualifiedName) {
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        PsiClass psiClass = javaPsiFacade.findClass(classQualifiedName, scope);
        Collection<PsiClass> classes = ClassInheritorsSearch.search(psiClass,
                GlobalSearchScope.projectScope(project), true).findAll();
        return classes;
    }

    private void cacheWebSession(WebSession webSession) {
        cachedWebSession = webSession;
    }
}
