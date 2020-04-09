package org.websync.debugger;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.websync.debugger.commands.CommandTestSerializer;
import org.websync.logger.Logger;
import org.websync.debugger.commands.CommandInitProject;
import org.websync.debugger.commands.CommandTestAttributes;

import java.util.List;
import java.util.stream.Collectors;

import static org.websync.jdi.JdiElement.JDI_WEB_PAGE;

public class FileParser {
    public void parse(List<String> lines) {
        if (lines == null || lines.size() == 0) {
            Logger.print("Command file is empty.");
            return;
        }

        String command = lines.get(0);
        Logger.print(String.format("COMMAND '%s' is performing...", command));
        long startTime = System.nanoTime();
        switch (command) {
            case "a":
                Logger.print("COMMAND 'a' something doing.");
                break;
            case "b":
                Logger.print("COMMAND 'b' something doing.");
                break;
            case "print classes":
                printClasses();
                break;
            case "test session":
                testWebSessionProvider();
                break;
            case "test serializer":
//                testSerializer();
                CommandTestSerializer.run();
                break;
            case "print components":
                testPrintComponents();
                break;
            case "test fields":
                testFieldsOfPsiClasses();
                break;
            case "test attributes":
                CommandTestAttributes.run();
                break;
            case "init project":
                ApplicationManager.getApplication().runReadAction(() -> {
                    CommandInitProject.run();
                });
                break;
            default:
                Logger.print(String.format("COMMAND '%s' is unknown", command));
        }
        long endTime = System.nanoTime();
        Logger.print(String.format("COMMAND '%s' is performed. Time = %.3f s.", command,
                (double) (endTime - startTime) / 1000000000));
    }

    private void testSerializer() {
//        ApplicationManager.getApplication().runReadAction(() -> {
//            Project project = ProjectManager.getInstance().getOpenProjects()[0];
//            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
//
//            WebSession session = webProvider.getWebSession(false);
//
//            WebSessionSerializer serializer = new ReactSerializer();
//            String json = serializer.serialize(session);
//            session = serializer.deserialize(json);
//        });
    }

    private void testWebSessionProvider() {
//        ApplicationManager.getApplication().runReadAction(() -> {
//            Project project = ProjectManager.getInstance().getOpenProjects()[0];
//            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
//            webProvider.getWebSession(true);
//        });
    }

    private void printClasses() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length == 0) {
            ApplicationManager.getApplication().runReadAction(() -> {
                Logger.print("None project has not been opened.");
            });
        }

        Project project = projects[0];
        if (!project.isInitialized()) {
            ApplicationManager.getApplication().runReadAction(() -> {
                Logger.print("Project has not been initialized.");
            });
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);

        ApplicationManager.getApplication().runReadAction(() -> {
            PsiClass webPagePsiClass = javaPsiFacade.findClass(JDI_WEB_PAGE.className, allScope);
            List<PsiClass> classes = ClassInheritorsSearch.search(webPagePsiClass).findAll()
                    .stream().collect(Collectors.toList());

            Logger.print("Classes:");
            classes.forEach(c -> Logger.print("*" + c.getQualifiedName()));
        });
    }

    private void testPrintComponents() {
//        ApplicationManager.getApplication().runReadAction(() -> {
//            Project project = ProjectManager.getInstance().getOpenProjects()[0];
//            WebSession session = new PsiWebSessionProvider(project).getWebSession(false);
//            Map<String, Component> components = session.getComponents();
//
//            Logger.print(String.format("Components: %s", components.size()));
//            components.forEach((k, v) -> {
//                String componentName = k;
//                String baseComponentId = v.getBaseComponentId() == null ? "" : v.getBaseComponentId();
//                Logger.print(String.format("%s - %s", componentName, baseComponentId));
//            });
//        });
    }

    private void testFieldsOfPsiClasses() {
//        ApplicationManager.getApplication().runReadAction(() -> {
//            Project project = ProjectManager.getInstance().getOpenProjects()[0];
//            WebSession session = new PsiWebSessionProvider(project).getWebSession(false);
//            Map<String, Component> components = session.getComponents();
//
//            String elementId = components.keySet().stream().filter(k -> k.contains("AttributesTest")).findFirst().get();
//            PsiComponent psiComponent = (PsiComponent) components.get(elementId);
//
//            Logger.print("Attributes:");
//            psiComponent.getComponentInstances().stream().forEach(instance -> {
//                String attr = ((PsiComponentInstance) instance).getId();
//                Logger.print("\t" + attr);
//                InstanceAnnotation instanceAnnotation = ((PsiComponentInstance) instance).getInstanceAttribute();
//                Logger.print(instanceAnnotation);
//
////                Object locator = instance.getLocator().get();
////                if (locator instanceof PsiComponentInstance.ByText) {
////                    PsiComponentInstance.ByText l = (PsiComponentInstance.ByText) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.Css) {
////                    PsiComponentInstance.Css l = (PsiComponentInstance.Css) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.JDropdown) {
////                    PsiComponentInstance.JDropdown l = (PsiComponentInstance.JDropdown) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.JTable) {
////                    PsiComponentInstance.JTable l = (PsiComponentInstance.JTable) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.JMenu) {
////                    PsiComponentInstance.JMenu l = (PsiComponentInstance.JMenu) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.UI) {
////                    PsiComponentInstance.UI l = (PsiComponentInstance.UI) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.WithText) {
////                    PsiComponentInstance.WithText l = (PsiComponentInstance.WithText) locator;
////                    Logger.print("\t\t" + l.toString());
////                } else if (locator instanceof PsiComponentInstance.XPath) {
////                    PsiComponentInstance.XPath l = (PsiComponentInstance.XPath) locator;
////                    Logger.print("\t\t" + l.toString());
////                }
//            });
//        });
    }
}

