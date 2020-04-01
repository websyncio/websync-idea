package org.websync.browserConnection.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.websync.WebSyncService;

import java.util.List;
import java.util.stream.Collectors;


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
        List<String> projects = webSyncService.getProvider().getProjects().stream().map(Project::getName).collect(Collectors.toList());
        if (projects.isEmpty()) {
            return;
        }
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = webSyncService.getSerializer().serialize(webSyncService.getProvider().getWebSessions(false));
        });
        json[0] = json[0].replaceFirst("\\{", String.format("{\"project\": %s,", projects.get(0)));
        webSyncService.getBrowserConnection().broadcast(json[0]);
    }

    public void execute(String projectName) {
        final String[] json = new String[1];
        int index = webSyncService.getProvider().getProjects().stream().map(Project::getName).collect(Collectors.toList()).indexOf(projectName);
        if (index < 0) {
            return;
        }
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = webSyncService.getSerializer().serialize(webSyncService.getProvider().getWebSession(
                    webSyncService.getProvider().getProjects().get(index)));
        });
        json[0] = json[0].replaceFirst("\\{", String.format("{\"project\": \"%s\",", projectName));
        webSyncService.getBrowserConnection().broadcast(json[0]);
    }
}