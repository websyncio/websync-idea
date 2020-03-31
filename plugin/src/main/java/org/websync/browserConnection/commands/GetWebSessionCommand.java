package org.websync.browserConnection.commands;

import com.intellij.openapi.application.ApplicationManager;
import org.java_websocket.WebSocket;
import org.websync.WebSyncService;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.websession.WebSessionPovider;


public class GetWebSessionCommand {
    private final WebSyncService webSyncService;

    public GetWebSessionCommand(WebSyncService webSyncService) {
        this.webSyncService = webSyncService;
    }
//
//    public static void run1(WebSocket conn) {
//        Project project = ProjectManager.getInstance().getOpenProjects()[0];
//        WebSession webSession = PsiWebSessionProvider.getWebSession(project);
//        Map<String, Component> components = webSession.getComponents();
//
//        String componentName = "AttributesTest";
//        String componentId = components.keySet().stream()
//                .filter(k -> k.contains(componentName))
//                .findFirst().get();
//        PsiComponent psiComponent = (PsiComponent) components.get(componentId);
//        ComponentDto componentDto = new ComponentDto(psiComponent);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        String serializedObject = null;
//        try {
//            serializedObject = mapper.writeValueAsString(componentDto);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        conn.send(serializedObject);
//    }

    public void execute() {
        final String[] json = new String[1];
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = webSyncService.getSerializer().serialize(
                    webSyncService.getProvider().getWebSessions(false));
        });
        webSyncService.getBrowserConnection().broadcast(json[0]);
    }
}