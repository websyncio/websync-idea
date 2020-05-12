package org.websync.debugger;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import org.websync.debugger.commands.CommandTestSerializerUtils;
import org.websync.logger.LoggerUtils;
import org.websync.debugger.commands.CommandInitProjectUtils;
import org.websync.debugger.commands.CommandTestAttributes;
import org.websync.websession.PsiJdiModulesProvider;
import org.websync.websession.models.ComponentType;
import org.websync.websession.models.JdiModule;
import org.websync.websession.psimodels.PsiComponentInstance;
import org.websync.websession.psimodels.PsiComponentType;
import org.websync.websession.psimodels.psi.AnnotationInstance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.websync.jdi.JdiElement.JDI_WEB_PAGE;

public class FileParser {
    public void parse(List<String> lines) {
        if (lines == null || lines.size() == 0) {
            LoggerUtils.print("Command file is empty.");
            return;
        }

        String command = lines.get(0);
        LoggerUtils.print(String.format("COMMAND '%s' is performing...", command));
        long startTime = System.nanoTime();
        switch (command) {
            case "a":
                LoggerUtils.print("COMMAND 'a' something doing.");
                break;
            case "b":
                LoggerUtils.print("COMMAND 'b' something doing.");
                break;
            case "print classes":
                printClasses();
                break;
            case "test serializer":
//                testSerializer();
                CommandTestSerializerUtils.run();
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
                    CommandInitProjectUtils.run();
                });
                break;
            default:
                LoggerUtils.print(String.format("COMMAND '%s' is unknown", command));
                break;
        }
        long endTime = System.nanoTime();
        LoggerUtils.print(String.format("COMMAND '%s' is performed. Time = %.3f s.", command,
                (double) (endTime - startTime) / 1000000000));
    }

    private void printClasses() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects.length == 0) {
            ApplicationManager.getApplication().runReadAction(() -> {
                LoggerUtils.print("None project has not been opened.");
            });
        }

        Project project = projects[0];
        if (!project.isInitialized()) {
            ApplicationManager.getApplication().runReadAction(() -> {
                LoggerUtils.print("Project has not been initialized.");
            });
        }

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        GlobalSearchScope allScope = GlobalSearchScope.allScope(project);

        ApplicationManager.getApplication().runReadAction(() -> {
            PsiClass webPagePsiClass = javaPsiFacade.findClass(JDI_WEB_PAGE.className, allScope);
            List<PsiClass> classes = ClassInheritorsSearch.search(webPagePsiClass).findAll()
                    .stream().collect(Collectors.toList());

            LoggerUtils.print("Classes:");
            classes.forEach(c -> LoggerUtils.print("*" + c.getQualifiedName()));
        });
    }

    private void testPrintComponents() {
        ApplicationManager.getApplication().runReadAction(() -> {
            String fullName = getFullNameForModuleInProject();
            JdiModule session = new PsiJdiModulesProvider().getJdiModule(fullName);
            Map<String, ComponentType> components = session.getComponentTypes();

            LoggerUtils.print(String.format("Components: %s", components.size()));
            components.forEach((Name, component) -> {
                String baseComponentId = component.getBaseComponentTypeId() == null ? "" : component.getBaseComponentTypeId();
                LoggerUtils.print(String.format("%s - %s", Name, baseComponentId));
            });
        });
    }

    private void testFieldsOfPsiClasses() {
        ApplicationManager.getApplication().runReadAction(() -> {
            String fullName = getFullNameForModuleInProject();
            JdiModule session = new PsiJdiModulesProvider().getJdiModule(fullName);
            Map<String, ComponentType> components = session.getComponentTypes();

            String elementId = components.keySet().stream().filter(k -> k.contains("AttributesTest")).findFirst().get();
            PsiComponentType psiComponent = (PsiComponentType) components.get(elementId);

            LoggerUtils.print("Attributes:");
            psiComponent.getComponentInstances().stream().forEach(instance -> {
                String attr = ((PsiComponentInstance) instance).getId();
                LoggerUtils.print("\t" + attr);
                AnnotationInstance instanceAnnotation = ((PsiComponentInstance) instance).getAttributeInstance();
                LoggerUtils.print(instanceAnnotation.toString());
            });
        });
    }

    private static String getFullNameForModuleInProject() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        Module module = ModuleManager.getInstance(project).getModules()[0];
        return project.getName() + "/" + module.getName();
    }
}
