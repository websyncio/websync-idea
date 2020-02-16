package com.epam.sha.intellij.websync.utils;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.MessageView;
import com.jediterm.terminal.ui.TerminalPanel;

import java.util.Arrays;
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
            default:
                System.out.println(String.format("Any command '%s'", lines.get(0)));
        }
    }

    private void printClasses() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
//        String[] classes = PsiShortNamesCache.getInstance(project).getAllClassNames();

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);

        ApplicationManager.getApplication().runReadAction(() -> {
            List<PsiClass> classes = AllClassesSearch.search(projectScope, project).findAll().stream()
                    .filter(c -> Arrays.asList(c.getSupers()).stream()
                            .filter(s -> s.getQualifiedName().equals("com.epam.jdi.light.elements.composite.WebPage"))
                            .count() > 0)
                    .collect(Collectors.toList());

            System.out.println("Classes:");
            classes.forEach(c -> System.out.println("*" + c.getQualifiedName()));
        });
    }
}
