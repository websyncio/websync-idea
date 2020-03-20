package org.websync.server.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.java_websocket.WebSocket;
import org.websync.ember.dto.ComponentDto;
import org.websync.websession.PsiWebSessionProvider;
import org.websync.websession.models.Component;
import org.websync.websession.models.WebSession;
import org.websync.websession.psimodels.PsiComponent;

import java.util.Map;

public class CommandGiveMePageObject {
    public static void run(WebSocket conn) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        WebSession webSession = PsiWebSessionProvider.getWebSession(project);
        Map<String, Component> components = webSession.getComponents();

        String componentName = "AttributesTest";
        String componentId = components.keySet().stream()
                .filter(k -> k.contains(componentName))
                .findFirst().get();
        PsiComponent psiComponent = (PsiComponent) components.get(componentId);
        ComponentDto componentDto = new ComponentDto(psiComponent);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String serializedObject = null;
        try {
            serializedObject = mapper.writeValueAsString(componentDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        conn.send(serializedObject);
    }
}
