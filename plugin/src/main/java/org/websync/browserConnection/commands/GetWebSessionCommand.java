package org.websync.browserConnection.commands;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.websync.browserConnection.WebSessionSerializer;
import org.websync.websession.WebSessionProvider;

import java.util.List;
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
        List<String> projects = webSessionProvider.getProjects().stream().map(Project::getName).collect(Collectors.toList());
        if (projects.isEmpty()) {
            return null;
        }
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = this.serializer.serialize(webSessionProvider.getWebSessions(false));
        });
        json[0] = json[0].replaceFirst("\\{", String.format("{\"project\": %s,", projects.get(0)));
        return json[0];
    }

    public String execute(String projectName) {
        final String[] json = new String[1];
        int index = webSessionProvider.getProjects().stream().map(Project::getName).collect(Collectors.toList()).indexOf(projectName);
        if (index < 0) {
            return null;
        }
        ApplicationManager.getApplication().runReadAction(() -> {
            json[0] = this.serializer.serialize(webSessionProvider.getWebSession(webSessionProvider.getProjects().get(index)));
        });
        json[0] = json[0].replaceFirst("\\{", String.format("{\"project\": \"%s\",", projectName));
        return json[0];
    }

}