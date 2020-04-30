package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.websync.debugger.testengine.TestEngineUtils;
import org.websync.websession.PsiJdiModulesProvider;
import org.websync.websession.models.ComponentType;
import org.websync.websession.models.JdiModule;
import org.websync.websession.psimodels.PsiComponentType;
import org.websync.websession.psimodels.PsiComponentInstance;
import org.websync.websession.psimodels.psi.AnnotationInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandTestAttributes {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            TestEngineUtils.run(CommandTestAttributes.class);
        });
    }

    private static boolean testValidNamesOfAttributesInComponent(Map<String, ComponentType> components, String componentsName) {
        // GIVEN
        String givenComponentName = componentsName;

        // Get PsiComponent object by given component name
        String componentId = components.keySet().stream()
                .filter(k -> k.contains(givenComponentName))
                .findFirst().get();
        PsiComponentType psiComponent = (PsiComponentType) components.get(componentId);

        // STEPS
        // Get attribute names of component instances
        List<String> actualAttributeNames = psiComponent.getComponentInstances().stream()
                .map(instance -> {
                    AnnotationInstance annotationInstance =
                            ((PsiComponentInstance) instance).getAttributeInstance();
                    return annotationInstance.getCodeReferenceElement();
                })
                .collect(Collectors.toList());

        // ASSERT
        List<String> expectedResults = Arrays.asList(
                "ByText", "Css", "JDropdown", "JMenu", "JTable", "UI", "UI.List", "WithText", "XPath",
                "FindBy", "FindBys",
                "Frame", "Name", "Title");

        MatcherAssert.assertThat(actualAttributeNames, Matchers.everyItem(Matchers.is(Matchers.in(expectedResults))));
        return true;
    }

    @Test
    public void testValidNamesOfAttributesInComponent() {
        //PREREQUISITES
        JdiModule jdiModule = new PsiJdiModulesProvider().getJdiModule(getFullNameForModuleInProject());
        Map<String, ComponentType> components = jdiModule.getComponentTypes();

        // TEST FOR GIVEN COMPONENT NAME
        String givenComponentName = "AttributesTest";


        Assert.assertTrue(testValidNamesOfAttributesInComponent(components, givenComponentName));
    }

    private static String getFullNameForModuleInProject() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        Module module = ModuleManager.getInstance(project).getModules()[0];
        return project.getName() + "/" + module.getName();
    }
}
