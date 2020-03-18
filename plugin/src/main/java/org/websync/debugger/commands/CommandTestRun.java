package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import javafx.util.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.websync.debugger.testengine.TestEngine;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.models.Component;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponent;
import org.websync.websession.psimodels.PsiComponentInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandTestRun {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            WebSession session = PsiWebSessionProvider.getWebSession(project);

            TestEngine.run(
                    new Pair<>(() -> testValidNamesOfAttributesInComponent(session),
                            "Test valid names of attributes in component"),
                    new Pair<>(() -> testValidNamesOfAttributesInSeveralComponents(session),
                            "Test valid names of attributes in several components"),
                    new Pair<>(() -> test2(session), "test2")
            );
        });
    }

    // Test valid names of attributes
    static void testValidNamesOfAttributesInComponent(WebSession webSession) {
        // GIVEN
        Map<String, Component> components = webSession.getComponents();
        String givenComponentName = "AttributesTest";

        // Get PsiComponent object by given component name
        String componentId = components.keySet().stream()
                .filter(k -> k.contains(givenComponentName))
                .findFirst().get();
        PsiComponent psiComponent = (PsiComponent) components.get(componentId);

        // STEPS
        // Get attribute names of component instances
        List<String> actualAttributeNames = psiComponent.getComponentInstances().stream()
                .map(instance -> {
                    PsiComponentInstance.InstanceAnnotation instanceAnnotation =
                            ((PsiComponentInstance) instance).getInstanceAttribute();
                    return instanceAnnotation.getCodeReferenceElement();
                })
                .collect(Collectors.toList());

        // ASSERT
        List<String> expectedResults = Arrays.asList(
                "ByText", "Css", "JDropdown", "JMenu", "JTable", "UI", "UI.List", "WithText", "XPath",
                "FindBy", "FindBys",
                "Frame", "Name", "Title");

        MatcherAssert.assertThat(actualAttributeNames, Matchers.everyItem(Matchers.is(Matchers.in(expectedResults))));
    }

    static void testValidNamesOfAttributesInSeveralComponents(WebSession webSession) {
        // PREREQUISITE
        Map<String, Component> components = webSession.getComponents();
        List<String> givenComponentNames = Arrays.asList("CustomElement", "CustomBaseElement");

        // TESTS
        givenComponentNames.stream().forEach(n -> {
            TestEngine.run(
                    new Pair<>(() -> testValidNamesOfAttributesInComponent(components, n),
                            String.format("Test valid names of attributes in component '%s'", n))
            );
        });
    }

    static void testValidNamesOfAttributesInComponent(Map<String, Component> components, String componentsName) {
        // GIVEN
        String givenComponentName = componentsName;

        // Get PsiComponent object by given component name
        String componentId = components.keySet().stream()
                .filter(k -> k.contains(givenComponentName))
                .findFirst().get();
        PsiComponent psiComponent = (PsiComponent) components.get(componentId);

        // STEPS
        // Get attribute names of component instances
        List<String> actualAttributeNames = psiComponent.getComponentInstances().stream()
                .map(instance -> {
                    PsiComponentInstance.InstanceAnnotation instanceAnnotation =
                            ((PsiComponentInstance) instance).getInstanceAttribute();
                    return instanceAnnotation.getCodeReferenceElement();
                })
                .collect(Collectors.toList());

        // ASSERT
        List<String> expectedResults = Arrays.asList(
                "ByText", "Css", "JDropdown", "JMenu", "JTable", "UI", "UI.List", "WithText", "XPath",
                "FindBy", "FindBys",
                "Frame", "Name", "Title");

        MatcherAssert.assertThat(actualAttributeNames, Matchers.everyItem(Matchers.is(Matchers.in(expectedResults))));
    }

    private static void test2(WebSession webSession) {
        MatcherAssert.assertThat(true, Matchers.is(false));
    }
}
