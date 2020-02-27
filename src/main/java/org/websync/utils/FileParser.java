package org.websync.utils;

import org.websync.browserConnection.SessionWebSerializer;
import org.websync.ember.EmberSerializer;
import org.websync.sessionweb.PsiSessionWebProvider;
import org.websync.sessionweb.models.Component;
import org.websync.sessionweb.models.SessionWeb;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileParser {
    public void parse(List<String> lines) {
        if (lines == null || lines.size() == 0) {
            System.out.println("File is empty.");
            return;
        }

        String command = lines.get(0);
        switch (command) {
            case "a":
                System.out.println(String.format("Command '%s'", command));
                break;
            case "b":
                System.out.println(String.format("Command '%s'", command));
                break;
            case "print":
                System.out.println(String.format(command));
                printClasses();
                break;
            case "web":
                System.out.println(String.format("%s...", command));
                testSessionWebProvider();
                break;
            case "serialize":
                System.out.println(String.format("%s...", command));
                testSerializer();
                break;
            case "print components":
                System.out.println(String.format("%s...", command));
                testPrintComponents();
                break;
            default:
                System.out.println(String.format("CommandNotExpectedException '%s'", command));
        }
    }

    private void testSerializer() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiSessionWebProvider webProvider = new PsiSessionWebProvider(project);

            Collection<SessionWeb> sessions = webProvider.getSessionWebs(false);

            SessionWebSerializer serializer = new EmberSerializer();
            String json = serializer.serialize(sessions);
            sessions = serializer.deserialize(json);
        });
    }

    private void testSessionWebProvider() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiSessionWebProvider webProvider = new PsiSessionWebProvider(project);
            webProvider.getSessionWebs(true);
        });
    }

    final public String JDI_WEBPAGE = "com.epam.jdi.light.elements.composite.WebPage";

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
            PsiClass webPagePsiClass = javaPsiFacade.findClass(JDI_WEBPAGE, allScope);
            List<PsiClass> classes = ClassInheritorsSearch.search(webPagePsiClass).findAll()
                    .stream().collect(Collectors.toList());

            System.out.println("Classes:");
            classes.forEach(c -> System.out.println("*" + c.getQualifiedName()));
        });
    }

    private void testPrintComponents() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiSessionWebProvider webProvider = new PsiSessionWebProvider(project);
            Collection<SessionWeb> sessions = webProvider.getSessionWebs(true);
            SessionWeb session = sessions.stream().findFirst().get();
            Map<String, Component> components = session.getComponentTypes();

            components.forEach((k, v) -> {
                System.out.println(k);
//                System.out.println(v.);
            });
        });
    }
}
