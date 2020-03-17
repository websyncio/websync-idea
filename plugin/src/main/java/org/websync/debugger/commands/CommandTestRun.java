package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.websync.debugger.testengine.TestEngine;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.models.Component;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponent;
import org.websync.websession.psimodels.PsiComponentInstance;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandTestRun {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
            Collection<WebSession> sessions = webProvider.getWebSessions(true);
            WebSession session = sessions.stream().findFirst().get();
            Map<String, Component> components = session.getComponents();

            TestEngine.run(() -> test1(components), "test1");
        });
    }

    // Test valid names of attributes
    private static void test1(Map<String, Component> components) {
//        List<String> givenPageNames = Arrays.asList("AttributesTest", "AttributesInitialization");
        List<String> givenPageNames = Arrays.asList("AttributesTest");
        List<String> expectedResults = Arrays.asList(
                "ByText", "Css", "JDropdown", "JMenu", "JTable", "UI", "UI.List", "WithText", "XPath",
                "FindBy", "FindBys",
                "Frame", "Name", "Title");

        givenPageNames.stream().forEach(pageName -> {
            String componentId = components.keySet().stream()
                    .filter(k -> k.contains(pageName))
                    .findFirst().get();
            PsiComponent psiComponent = (PsiComponent) components.get(componentId);

            psiComponent.getComponentInstances().stream().forEach(instance -> {
                String attr = ((PsiComponentInstance) instance).getId();
                PsiComponentInstance.InstanceAnnotation instanceAnnotation =
                        ((PsiComponentInstance) instance).getInstanceAttribute();

                String actualResult = instanceAnnotation.getCodeReferenceElement();
                MatcherAssert.assertThat(expectedResults, Matchers.hasItem(actualResult));
            });
        });
    }
}
