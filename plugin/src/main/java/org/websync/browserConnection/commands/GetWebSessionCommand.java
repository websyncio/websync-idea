package org.websync.browserConnection.commands;

import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.websession.WebSessionProvider;

import java.util.stream.Collectors;


public class GetWebSessionCommand {
    private final WebSessionSerializer serializer;
    private final WebSessionProvider webSessionProvider;

    public GetWebSessionCommand(WebSessionProvider webSessionProvider, WebSessionSerializer serializer) {
        this.webSessionProvider = webSessionProvider;
        this.serializer = serializer;
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

    public String execute() {
        final String[] json = new String[1];
        String projects = new Gson().toJson(webSessionProvider.getProjects().stream().map(Project::getName).collect(Collectors.toList()));
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = this.serializer.serialize(webSessionProvider.getWebSessions(false));
        });
        json[0] = json[0].replaceFirst("\\{", String.format("{\"projects\": %s,", projects));
        return json[0];
    }
}