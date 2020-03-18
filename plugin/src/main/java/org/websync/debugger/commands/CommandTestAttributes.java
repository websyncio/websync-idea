package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import javafx.util.Pair;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
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

public class CommandTestAttributes {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            TestEngine.run(CommandTestAttributes.class);
        });
    }

    @Test
    public static void testValidNamesOfAttributesInSeveralComponents() {
        // PREREQUISITE
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        WebSession webSession = PsiWebSessionProvider.getWebSession(project);

        Map<String, Component> components = webSession.getComponents();
        List<String> givenComponentNames = Arrays.asList("CustomElement", "CustomBaseElement");

        // TESTS FOR EACH GIVEN COMPONENT NAME
        givenComponentNames.stream().forEach(name -> {
            TestEngine.run(
                    new Pair<>(
                            () -> testValidNamesOfAttributesInComponent(components, name),
                            String.format("%s for component '%s'", "testValidNamesOfAttributesInComponent", name)
                    )
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

    @Test
    public static void testDummyAndFailed() {
        MatcherAssert.assertThat(true, Matchers.is(false));
    }

    @Test
    public static void testValidNamesOfAttributesInComponent() {
        // PREREQUISITES
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        WebSession webSession = PsiWebSessionProvider.getWebSession(project);
        Map<String, Component> components = webSession.getComponents();

        // TEST FOR GIVEN COMPONENT NAME
        String givenComponentName = "AttributesTest";

        testValidNamesOfAttributesInComponent(components, givenComponentName);
    }

    @Test
    public static void testValidNamesOfAttributesInPages() {

    }
}
