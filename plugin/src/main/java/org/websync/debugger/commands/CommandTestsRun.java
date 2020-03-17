package org.websync.debugger.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.models.Component;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponent;
import org.websync.websession.psimodels.PsiComponentInstance;

import java.util.Collection;
import java.util.Map;

public class CommandTestsRun {
    public static void run() {
        ApplicationManager.getApplication().runReadAction(() -> {
            Project project = ProjectManager.getInstance().getOpenProjects()[0];
            PsiWebSessionProvider webProvider = new PsiWebSessionProvider(project);
            Collection<WebSession> sessions = webProvider.getWebSessions(true);
            WebSession session = sessions.stream().findFirst().get();
            Map<String, Component> components = session.getComponents();

            String elementId = components.keySet().stream().filter(k -> k.contains("AttributesTest")).findFirst().get();
            PsiComponent psiComponent = (PsiComponent) components.get(elementId);

            System.out.println("Attributes:");
            psiComponent.getComponentInstances().stream().forEach(instance -> {
                String attr = ((PsiComponentInstance) instance).getId();
                System.out.println("\t" + attr);
                PsiComponentInstance.Annotation annotation = ((PsiComponentInstance) instance).getAttribute();
                System.out.println(annotation);
            });
        });
    }
}
