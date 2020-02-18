package com.epam.websync.utils;

import com.epam.websync.sessionweb.PsiSessionWebProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;

import java.util.List;
import java.util.stream.Collectors;

public class FileParser {
    public void parse(List<String> lines) {
        if (lines == null || lines.size() == 0) {
            System.out.println("File is empty.");
            return;
        }

        switch (lines.get(0)) {
            case "a":
                System.out.println("Command 'a'");
                break;
            case "b":
                System.out.println("Command 'b'");
                break;
            case "print":
                System.out.println("Print");
                printClasses();
                break;
            case "web":
                System.out.println("web...");
                testSessionWebProvider();
            default:
                System.out.println(String.format("Any command '%s'", lines.get(0)));
        }
    }
    private void testSessionWebProvider() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        PsiSessionWebProvider webProvider = new PsiSessionWebProvider(project);
        webProvider.getSessionWebs(false);
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
}
