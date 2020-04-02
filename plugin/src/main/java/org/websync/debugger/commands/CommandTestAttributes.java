package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.websync.debugger.testengine.TestEngine;
import org.websync.websession.models.ComponentType;
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
            TestEngine.run(CommandTestAttributes.class);
        });
    }

    @Test
    public static void testValidNamesOfAttributesInSeveralComponents() {
//        // PREREQUISITE
//        Project project = ProjectManager.getInstance().getOpenProjects()[0];
//        List<WebSession> webSession = new PsiWebSessionProvider().getWebSessions(false);
//
//        Map<String, Component> components = webSession.getComponents();
//        List<String> givenComponentNames = Arrays.asList("CustomElement", "CustomBaseElement");
//
//        // TESTS FOR EACH GIVEN COMPONENT NAME
//        givenComponentNames.stream().forEach(name -> {
//            Method method = null;
//            try {
//                method = CommandTestAttributes.class.getDeclaredMethod("testValidNamesOfAttributesInComponent",
//                        Map.class, String.class);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//            TestEngine.run(method, components, name);
//        });
    }

    static void testValidNamesOfAttributesInComponent(Map<String, ComponentType> components, String componentsName) {
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
    }

    @Test
    public static void testDummyAndFailed() {
        MatcherAssert.assertThat(true, Matchers.is(false));
    }

    @Test
    public static void testValidNamesOfAttributesInComponent() {
//        // PREREQUISITES
//        Project project = ProjectManager.getInstance().getOpenProjects()[0];
//        WebSession webSession = new PsiWebSessionProvider(project).getWebSession(false);
//        Map<String, Component> components = webSession.getComponents();
//
//        // TEST FOR GIVEN COMPONENT NAME
//        String givenComponentName = "AttributesTest";
//
//        testValidNamesOfAttributesInComponent(components, givenComponentName);
    }

    @Test
    public static void testValidNamesOfAttributesInPages() {

    }
}
