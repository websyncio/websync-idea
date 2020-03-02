package org.websync.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.InheritanceUtil;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.ember.EmberSerializer;
import org.websync.jdi.JdiElement;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.models.Component;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.websync.jdi.JdiElement.JDI_WEB_PAGE;

public class FileParser {
    public void parse(List<String> lines) {
        if (lines == null || lines.size() == 0) {
            System.out.println("Command file is empty.");
            return;
        }

        String command = lines.get(0);
        System.out.println(String.format("Command '%s' is performing...", command));
        switch (command) {
            case "a":
                System.out.println(String.format("Command '%s' performed", command));
                break;
            case "b":
                System.out.println(String.format("Command '%s' performed", command));
                break;
            case "print classes":
                printClasses();
                break;
            case "test session":
                testWebSessionProvider();
                break;
            case "test serializer":
                testSerializer();
                break;
            case "print components":
                testPrintComponents();
                break;
            case "test fields":
                testFieldsOfPsiClasses();
                break;
            default:
                System.out.println(String.format("Unknown command '%s' is detected", command));
        }
        System.out.println(String.format("Command '%s' is performed", command));
    }

    private void testSerializer() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);

            Collection<WebSession> sessions = webProvider.getWebSessions(false);

            WebSessionSerializer serializer = new EmberSerializer();
            String json = serializer.serialize(sessions);
            sessions = serializer.deserialize(json);
        });
    }

    private void testWebSessionProvider() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
            webProvider.getWebSessions(true);
        });
    }

    private void printClasses() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length == 0) {
            ApplicationManager.getApplication().runReadAction(() -> {
                System.out.println("None project has not been opened.");
            });
        }

        Project project = projects[0];
        if (!project.isInitialized()) {
            ApplicationManager.getApplication().runReadAction(() -> {
                System.out.println("Project has not been initialized.");
            });
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);

        ApplicationManager.getApplication().runReadAction(() -> {
            PsiClass webPagePsiClass = javaPsiFacade.findClass(JDI_WEB_PAGE.value, allScope);
            List<PsiClass> classes = ClassInheritorsSearch.search(webPagePsiClass).findAll()
                    .stream().collect(Collectors.toList());

            System.out.println("Classes:");
            classes.forEach(c -> System.out.println("*" + c.getQualifiedName()));
        });
    }

    private void testPrintComponents() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
            Collection<WebSession> sessions = webProvider.getWebSessions(true);
            WebSession session = sessions.stream().findFirst().get();
            Map<String, Component> components = session.getComponents();

            System.out.println(String.format("Components: %s", components.size()));
            components.forEach((k, v) -> {
                String componentName = k;
                String baseComponentId = v.getBaseComponentId() == null ? "" : v.getBaseComponentId();
                System.out.println(String.format("%s - %s", componentName, baseComponentId));
            });
        });
    }

    private void testFieldsOfPsiClasses() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
            Collection<WebSession> sessions = webProvider.getWebSessions(true);
            WebSession session = sessions.stream().findFirst().get();
            Map<String, Component> components = session.getComponents();

            String elementId = components.keySet().stream().filter(k -> k.contains("MarvelUser")).findFirst().get();
            PsiComponent psiComponent = (PsiComponent) components.get(elementId);

            PsiField[] fields = psiComponent.getPsiClass().getFields();
            List<PsiField> fieldsList = Arrays.asList(fields);

            //
            fieldsList.stream().forEach(f -> {
                boolean isElement = Arrays.asList(f.getType().getSuperTypes())
                        .stream()
                        .anyMatch(s -> {
                            return InheritanceUtil.isInheritor(s, JdiElement.JDI_UI_BASE_ELEMENT.value);
                        });
                if (isElement) {
                    System.out.println(f.getName() + " is Element");
                }
            });
//            System.out.println("");
        });
    }

}

